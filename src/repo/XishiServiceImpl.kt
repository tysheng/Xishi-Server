package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.common.SEPARATOR
import com.tysheng.xishi.server.data.*
import com.tysheng.xishi.server.logD
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import repo.DatabaseFactory.dbQuery

class XishiServiceImpl : XishiService {
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

    override suspend fun retrieveAlbum(albumId: Int): Album {
        TODO()
    }

    override suspend fun addShot(shot: Shot): Shot? {
        TODO()
    }

    override suspend fun retrieveShot(shotId: Int): Shot {
        TODO()
    }

    override suspend fun retrieveShotsByAlbum(albumId: Int): List<Shot> {
        TODO()
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

    override suspend fun updateUser(user: User): User? {
        TODO()
    }

    override suspend fun retrieveUser(userId: Int): User? {
        TODO()
    }

    private suspend fun checkAlbumExists(albumId: Int): Boolean {
        return dbQuery {
            AlbumTable.select(AlbumTable.albumId.eq(albumId)).any()
        }
    }

    private fun addId(originalStr: String?, id: Int): Pair<String, Int> {
        return if (originalStr.isNullOrEmpty()) {
            id.toString() to 1
        } else {
            val set = originalStr!!.split(SEPARATOR).toMutableSet()
            set.add(id.toString())
            set.joinToString(separator = SEPARATOR) to set.size
        }
    }

    private fun deleteId(originalStr: String?, id: Int): Pair<String?, Int> {
        return if (originalStr.isNullOrEmpty()) {
            null to 0
        } else {
            val set = originalStr!!.split(SEPARATOR).toMutableSet()
            set.remove(id.toString())
            set.joinToString(separator = SEPARATOR) to set.size
        }
    }

    override suspend fun addAlbum(userId: Int, album: Album): Int {
        if (!checkAlbumExists(album.albumId)) {
            addAlbum(album)
        }
        return dbQuery {
            val resultRow = UserTable.select(UserTable.userId.eq(userId)).single()
            val (albumStr, size) = addId(resultRow[UserTable.albums], album.albumId)
            UserTable.update({
                UserTable.userId.eq(userId)
            }) {
                it[albums] = albumStr
                it[albumCount] = size
            }
            size
        }
    }

    override suspend fun deleteAlbum(userId: Int, albumId: Int): Int {
        return dbQuery {
            val resultRow = UserTable.select(UserTable.userId.eq(userId)).single()
            val (albumStr, size) = deleteId(resultRow[UserTable.albums], albumId)
            UserTable.update({
                UserTable.userId.eq(userId)
            }) {
                it[albums] = albumStr
                it[albumCount] = size
            }
            size
        }
    }

    override suspend fun addShot(userId: Int, shot: Shot): Int {
        TODO()
    }

    override suspend fun deleteShot(userId: Int, shotId: Int): Int {
        TODO()
    }
}