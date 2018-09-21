package com.tysheng.xishi.server.repo

data class UpdatePasswordServiceResult(
        val success:Boolean,
        val isWrongPassword: Boolean
)