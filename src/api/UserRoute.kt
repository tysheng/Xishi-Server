package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.common.xishiJwt
import com.tysheng.xishi.server.data.User
import com.tysheng.xishi.server.repo.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route



fun Route.user(service: UserService) {

    route("/user") {
        post("/register") {
            val param = call.receive<UserRegisterParam>()
            val newUser = User(userName = param.userName,
                    password = param.password)
            val result = service.addNewUser(newUser)
            result?.apply {
                token = xishiJwt.sign(userId)
            }
            if (result != null) {
                call.respond(HttpStatusCode.Created, CommonResponse(content = result))
            } else {
                call.respond(CommonResponse<Any>(CommonResponse.NOT_FOUNT))
            }
        }

        get("/all") {
            val needExtra = call.request.queryParameters["need_extra"]?.toBoolean() ?: false
            val allUsers = service.retrieveAllUsers(needExtra)
            call.respond(CommonResponse(CommonResponse.OK, allUsers))
        }

        jwtAuth {
            post("/edit-name") {
                call.checkUserId { userId ->
                    val param = receive<UpdateNameParam>()
                    val newName = param.newName
                    if (newName.isNotEmpty()) {
                        service.updateUserName(userId, newName)
                        respond(CommonResponse(content = newName))
                    } else {
                        respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
                    }
                }
            }
        }

        jwtAuth {
            post("/edit-password") {
                call.checkUserId { userId ->
                    val param = receive<UpdatePasswordParam>()
                    val newPassword = param.newPassword
                    val oldPassword = param.oldPassword
                    if (newPassword.isNotEmpty() && oldPassword.isNotEmpty()) {
                        val result = service.updatePassword(userId, oldPassword, newPassword)
                        if (result.success) {
                            respond(CommonResponse<Any>(CommonResponse.OK))
                        } else {
                            respond(CommonResponse<Any>(CommonResponse.WRONG_PASSWORD))
                        }
                    } else {
                        respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
                    }
                }
            }
        }
    }
}

