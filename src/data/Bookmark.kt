package com.tysheng.xishi.server.data

import org.jetbrains.exposed.sql.Table

object AlbumBookmarkTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val uniqueId = varchar("uniqueId", 30).uniqueIndex()
    val albumId = integer("albumId")
    val userId = integer("userId")
    val timestamp = long("timestamp")

}

object ShotBookmarkTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val uniqueId = varchar("uniqueId", 30).uniqueIndex()
    val shotId = integer("shotId")
    val userId = integer("userId")
    val timestamp = long("timestamp")
}

fun bookmarkUniqueId(userId:Int,id:Int):String ="${userId}_$id"
