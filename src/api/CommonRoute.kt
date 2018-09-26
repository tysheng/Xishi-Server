package com.tysheng.xishi.server.api

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get

fun Route.common() {
    get("/") {
        call.respond("welcome")
    }
}