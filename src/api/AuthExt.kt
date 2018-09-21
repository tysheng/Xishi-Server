package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.common.JWT
import io.ktor.auth.authenticate
import io.ktor.routing.Route

fun Route.jwtAuth(optional: Boolean = false, build: Route.() -> Unit): Route = authenticate(JWT, optional = optional, build = build)