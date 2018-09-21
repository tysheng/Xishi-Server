package com.tysheng.xishi.server.data

import org.jetbrains.exposed.sql.Table

object ShotTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val albumId = integer("albumId") references AlbumTable.albumId
    val shotId = integer("shotId")
    val title = text("title")
    val content = text("content")
    val url = varchar("url", 255)
    val author = varchar("author", 255)
    val imageSize = long("imageSize")
    val addTime = long("addTime")
    val thumb = varchar("thumb", 255)
    val youShotLink = varchar("youShotLink", 255)
}

data class Shot(
        val shotId: Int,
        val albumId: Int,
        val title: String,
        val content: String,
        val author: String,
        val url: String,
        val youShotLink: String? = null
)