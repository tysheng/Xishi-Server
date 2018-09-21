package com.tysheng.xishi.server.data

import org.jetbrains.exposed.sql.Table

object AlbumTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val albumId = integer("albumId").uniqueIndex()
    val title = text("title")
    val url = varchar("url", 255)
    val addTime = long("addTime")
}


data class Album(
        val albumId: Int,
        val title: String,
        val url: String,
        val addTime: Long
)