package com.tysheng.xishi.server.data

import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id = integer("id").primaryKey().autoIncrement()
    val userId = integer("userId").autoIncrement()
    val password = varchar("password", 50)
    val userName = varchar("userName", 30)
    val avatar = varchar("avatar", 255).nullable()
}

data class User(
        val userId: Int = 0,
        val userName: String,
        var password: String? = null,
        val avatar: String? = null,
        val albums: List<Album>? = null,
        val shots: List<Shot>? = null,
        var token: String? = null
)



