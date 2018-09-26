package com.tysheng.xishi.server.api

data class Resp<T>(
        val statusCode: Int = OK,
        val content: T? = null
) {
    companion object {
        const val OK = 200
        const val NOT_FOUNT = 1
        const val FORBIDDEN = 2
        const val PARAMS_ERROR = 3
        const val WRONG_PASSWORD = 101
    }
}

