package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.*
import org.jetbrains.exposed.sql.ResultRow
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


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


fun ResultRow.toUser(albums: List<Album>?, shots: List<Shot>?): User {
    return User(userId = this[UserTable.userId],
            userName = this[UserTable.userName],
            password = null,
            avatar = this[UserTable.avatar],
            albums = albums, shots = shots)
}

