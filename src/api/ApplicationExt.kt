package com.tysheng.xishi.server.api

import io.ktor.application.ApplicationCall
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authentication
import io.ktor.response.respond

fun ApplicationCall.userId(): Int? {
    return authentication.principal<UserIdPrincipal>()?.name?.toInt()
}

suspend fun ApplicationCall.checkUserId(block: suspend ApplicationCall.(userId: Int) -> Unit) {
    val userId = userId()
    if (userId != null) {
        block(userId)
    } else {
        respond(Resp<Any>(Resp.FORBIDDEN))
    }
}