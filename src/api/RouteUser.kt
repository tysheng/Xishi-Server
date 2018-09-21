package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.common.xishiJwt
import com.tysheng.xishi.server.data.User
import com.tysheng.xishi.server.data.UserRegisterParam
import com.tysheng.xishi.server.repo.UserService
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.auth.digestAuthenticationCredentials
import io.ktor.auth.jwt.JWTPrincipal
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.user(userService: UserService) {

    route("/user") {
        post("/register") {
            val param = call.receive<UserRegisterParam>()
            val newUser = User(userName = param.userName,
                    password = param.password)
            val result = userService.addNewUser(newUser)
            result?.apply {
                token = xishiJwt.sign(userId)
            }
            if (result != null) {
                call.respond(HttpStatusCode.Created, CommonResponse(content = result))
            } else {
                call.respond(CommonResponse<Any>(CommonResponse.NOT_FOUNT))
            }
        }

        jwtAuth {
            post("/edit-name") {
                val userId = call.authentication.principal<UserIdPrincipal>()?.name?.toInt()
                if (userId != null) {
                    val param = call.receive<Map<String, String>>()
                    val newName = param["new_name"]
                    if (newName?.isNotEmpty() == true) {
                        userService.updateUserName(userId, newName)
                        call.respond(CommonResponse(content = newName))
                    } else {
                        call.respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
                    }
                } else {
                    call.respond(CommonResponse<Any>(CommonResponse.FORBIDDEN))
                }
            }
        }
    }
}