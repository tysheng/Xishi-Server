package com.tysheng.xishi.server.api

data class UserRegisterParam(
        val userName: String,
        val password: String,
        val avatar: String? = null
)

class UpdateNameParam {
    lateinit var newName: String
}

data class UpdatePasswordParam(
        val oldPassword: String,
        val newPassword: String
)

data class AddAlbumParam(
        val albumId: Int,
        val title: String,
        val url: String,
        val addTime: Long
)

data class AddShotParam(
        val shotId: Int,
        val albumId: Int,
        val title: String,
        val content: String,
        val author: String,
        val url: String,
        val youShotLink: String? = null,
        val imageSize: Long,
        val addTime: Long,
        val thumb: String? = null
)

data class DeleteParam(
        val id: Int
)