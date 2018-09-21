package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.*
import org.jetbrains.exposed.sql.ResultRow


// 2018-06-08 10:42:06
//val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
//    timeZone = TimeZone.getTimeZone("GMT+8")
//}
//
//fun String.addTimeToMillis(): Long {
//    val date = timeFormat.parse(this)
//    return date.time
//}

fun ResultRow.toAlbum(): Album {
    return Album(this[AlbumTable.albumId],
            this[AlbumTable.title],
            this[AlbumTable.url],
            this[AlbumTable.addTime])
}

fun ResultRow.toShot(): Shot {
    return Shot(shotId = this[ShotTable.shotId],
            albumId = this[ShotTable.albumId],
            title = this[ShotTable.title],
            content = this[ShotTable.content],
            author = this[ShotTable.author],
            url = this[ShotTable.url],
            youShotLink = this[ShotTable.youShotLink],
            imageSize = this[ShotTable.imageSize],
            addTime = this[ShotTable.addTime],
            thumb = this[ShotTable.thumb]
    )
}


fun ResultRow.toUser(albums: List<Album>?, shots: List<Shot>?, hidePassword: Boolean = true): User {
    return User(userId = this[UserTable.userId],
            userName = this[UserTable.userName],
            password = if (hidePassword) null else this[UserTable.password],
            avatar = this[UserTable.avatar],
            albums = albums, shots = shots)
}

