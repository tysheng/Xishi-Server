package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import repo.DatabaseFactory.dbQuery

class XishiServiceImpl : XishiService {
    override suspend fun retrieveAllUsers(needExtra: Boolean): List<User>? {
        return dbQuery {
            UserTable.selectAll()
                    .map {
                        val (albums, shots) = if (needExtra) {
                            val userId = it[UserTable.userId]
                            allAlbums(userId) to allShots(userId)
                        } else {
                            null to null
                        }
                        it.toUser(albums, shots)
                    }
        }
    }

    override suspend fun addAlbum(album: Album): Album? {
        val id = dbQuery {
            AlbumTable.insert {
                it[albumId] = album.albumId
                it[title] = album.title
                it[url] = album.url
                it[addTime] = album.addTime
            }
        } get AlbumTable.id
        return if (id != null) {
            dbQuery { AlbumTable.select(AlbumTable.id.eq(id)).singleOrNull()?.toAlbum() }
        } else {
            null
        }
    }

    override suspend fun retrieveAlbum(albumId: Int): Album? {
        return dbQuery {
            AlbumTable.select(AlbumTable.albumId.eq(albumId)).singleOrNull()?.toAlbum()
        }
    }

    override suspend fun addShot(shot: Shot): Shot? {
        val id = dbQuery {
            ShotTable.insert {
                it[albumId] = shot.albumId
                it[title] = shot.title
                it[url] = shot.url
                it[addTime] = shot.addTime
                it[thumb] = shot.thumb
                it[youShotLink] = shot.youShotLink
                it[imageSize] = shot.imageSize
                it[author] = shot.author
                it[content] = shot.content
                it[shotId] = shot.shotId
            }
        } get ShotTable.id
        return if (id != null) {
            dbQuery { ShotTable.select(ShotTable.id.eq(id)).singleOrNull()?.toShot() }
        } else {
            null
        }
    }

    override suspend fun retrieveShot(shotId: Int): Shot? {
        return dbQuery {
            ShotTable.select(ShotTable.shotId.eq(shotId)).singleOrNull()?.toShot()
        }
    }

    override suspend fun retrieveShotsByAlbum(albumId: Int): List<Shot>? {
        return dbQuery {
            ShotTable.select(ShotTable.albumId.eq(albumId)).map {
                it.toShot()
            }
        }
    }

    override suspend fun addNewUser(user: User): User? {
        return dbQuery {
            val id = UserTable.insert {
                it[userName] = user.userName
                it[avatar] = user.avatar
                it[password] = user.password!!
            } get UserTable.id
            if (id != null) {
                UserTable.select(UserTable.id.eq(id)).singleOrNull()?.toUser(null, null)
                        ?.apply {
                            password = null
                        }
            } else {
                null
            }
        }
    }

    override suspend fun retrieveUser(userId: Int, needExtra: Boolean): User? {
        return dbQuery {
            val resultRow = UserTable.select(UserTable.userId.eq(userId)).single()
            var albums: List<Album>? = null
            var shots: List<Shot>? = null
            if (needExtra) {
                val albumList = AlbumBookmarkTable.select(AlbumBookmarkTable.userId.eq(userId))
                        .orderBy(AlbumBookmarkTable.timestamp, isAsc = false)
                        .map {
                            it[AlbumBookmarkTable.albumId]
                        }.toList()
                val shotList = ShotBookmarkTable.select(ShotBookmarkTable.userId.eq(userId))
                        .orderBy(ShotBookmarkTable.timestamp, isAsc = false)
                        .map {
                            it[ShotBookmarkTable.shotId]
                        }.toList()

                if (albumList.isNotEmpty()) {
                    albums = AlbumTable.select {
                        AlbumTable.albumId inList albumList
                    }.map { it.toAlbum() }.toList()
                }
                if (shotList.isNotEmpty()) {
                    shots = ShotTable.select {
                        ShotTable.albumId inList shotList
                    }.map { it.toShot() }.toList()
                }
            }
            resultRow.toUser(albums, shots)
        }
    }

    override suspend fun updateUserName(userId: Int, newName: String) {
        dbQuery {
            UserTable.update({ UserTable.userId eq userId }) {
                it[userName] = newName
            }
        }
    }

    override suspend fun updatePassword(userId: Int, oldPsw: String, newPsw: String): UpdatePasswordServiceResult {
        return dbQuery {
            val password = UserTable.select(UserTable.userId.eq(userId)).single()[UserTable.password]
            if (password != oldPsw) {
                return@dbQuery UpdatePasswordServiceResult(false, true)
            }
            UserTable.update({ UserTable.userId eq userId }) {
                it[this.password] = newPsw
            }
            UpdatePasswordServiceResult(true, false)
        }
    }

    private suspend fun checkAlbumExists(albumId: Int): Boolean {
        return dbQuery {
            AlbumTable.select(AlbumTable.albumId.eq(albumId)).any()
        }
    }

    override suspend fun addAlbum(userId: Int, album: Album): Int {
        if (!checkAlbumExists(album.albumId)) {
            addAlbum(album)
        }
        return dbQuery {
            val uniqueId = bookmarkUniqueId(userId, album.albumId)
            AlbumBookmarkTable.update({
                AlbumBookmarkTable.uniqueId eq uniqueId
            }) {
                it[timestamp] = System.currentTimeMillis()
            }.let { i ->
                if (i == 0) {
                    AlbumBookmarkTable.insert {
                        it[this.userId] = userId
                        it[albumId] = album.albumId
                        it[timestamp] = System.currentTimeMillis()
                        it[this.uniqueId] = uniqueId
                    }
                }
                1
            }
        }
    }

    override suspend fun deleteAlbum(userId: Int, albumId: Int): Int {
        return dbQuery {
            val bookmarkUniqueId = bookmarkUniqueId(userId, albumId)
            AlbumBookmarkTable.deleteWhere {
                AlbumBookmarkTable.uniqueId eq bookmarkUniqueId
            }
        }
    }

    override suspend fun addShot(userId: Int, shot: Shot): Int {
        val exist = dbQuery {
            ShotTable.select(ShotTable.shotId.eq(shot.shotId)).any()
        }
        if (!exist) {
            addShot(shot)
        }
        return dbQuery {
            val uniqueId = bookmarkUniqueId(userId, shot.shotId)
            ShotBookmarkTable.update({
                ShotBookmarkTable.uniqueId eq uniqueId
            }) {
                it[timestamp] = System.currentTimeMillis()
            }.let { i ->
                if (i == 0) {
                    ShotBookmarkTable.insert {
                        it[this.userId] = userId
                        it[shotId] = shot.shotId
                        it[timestamp] = System.currentTimeMillis()
                        it[this.uniqueId] = uniqueId
                    }
                }
                1
            }
        }
    }

    override suspend fun deleteShot(userId: Int, shotId: Int): Int {
        return dbQuery {
            val bookmarkUniqueId = bookmarkUniqueId(userId, shotId)
            ShotBookmarkTable.deleteWhere {
                ShotBookmarkTable.uniqueId eq bookmarkUniqueId
            }
        }
    }

    private fun allAlbums(userId: Int): List<Album>? {
        return if (userId > 0) {
            AlbumBookmarkTable.select(AlbumBookmarkTable.userId.eq(userId))
        } else {
            AlbumBookmarkTable.selectAll()
        }.map { it[AlbumBookmarkTable.albumId] }.let {
            AlbumTable.select {
                AlbumTable.albumId inList it
            }.map { it.toAlbum() }
        }
    }

    override suspend fun retrieveAllAlbums(userId: Int): List<Album>? {
        return dbQuery {
            allAlbums(userId)
        }
    }

    private fun allShots(userId: Int): List<Shot>? {
        return if (userId > 0) {
            ShotBookmarkTable.select(ShotBookmarkTable.userId.eq(userId))
        } else {
            ShotBookmarkTable.selectAll()
        }.map { it[ShotBookmarkTable.shotId] }.let {
            ShotTable.select {
                ShotTable.shotId inList it
            }.map { it.toShot() }
        }
    }

    override suspend fun retrieveAllShots(userId: Int): List<Shot>? {
        return dbQuery {
            allShots(userId)
        }
    }
}

