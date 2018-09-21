package com.tysheng.xishi.server.api

data class CommonResponse<T>(
        val statusCode: Int = OK,
        val content: T? = null
) {
    companion object {
        const val OK = 0
        const val NOT_FOUNT = 1
        const val FORBIDDEN = 2
        const val PARAMS_ERROR = 3
    }
}

