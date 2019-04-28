package com.tysheng.xishi.server

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.tysheng.xishi.server.api.common
import com.tysheng.xishi.server.api.gallery
import com.tysheng.xishi.server.api.user
import com.tysheng.xishi.server.common.CLAIM_USER_ID
import com.tysheng.xishi.server.common.JWT
import com.tysheng.xishi.server.common.xishiJwt
import com.tysheng.xishi.server.common.xishiService
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import repo.DatabaseFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)

    install(Authentication) {
        jwt(JWT) {
            verifier(xishiJwt.verifier)
            validate {
                val claim = it.payload.getClaim(CLAIM_USER_ID)
                UserIdPrincipal(claim.asInt().toString())
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
        }
    }

    DatabaseFactory.init(environment)
    install(Routing) {
        user(xishiService)
        gallery(xishiService)
        common()
    }
}

