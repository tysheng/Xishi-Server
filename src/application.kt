package com.tysheng.xishi.server

import com.fasterxml.jackson.annotation.JsonInclude
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.tysheng.xishi.server.api.user
import com.tysheng.xishi.server.common.CLAIM_USER_ID
import com.tysheng.xishi.server.common.JWT
import com.tysheng.xishi.server.common.xishiJwt
import com.tysheng.xishi.server.common.xishiService
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import io.ktor.features.*
import repo.DatabaseFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.DevelopmentEngine.main(args)


fun Application.module() {
    install(DefaultHeaders)
    install(CallLogging)

    install(Authentication) {
        jwt(JWT) {
            verifier(xishiJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim(CLAIM_USER_ID).asString())
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

    DatabaseFactory.init()
    install(Routing) {
        user(xishiService)
    }
}

