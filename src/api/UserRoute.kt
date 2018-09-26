package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.common.Clients
import com.tysheng.xishi.server.common.copyToSuspend
import com.tysheng.xishi.server.common.xishiJwt
import com.tysheng.xishi.server.data.User
import com.tysheng.xishi.server.logD
import com.tysheng.xishi.server.repo.UserService
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.request.receive
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import java.io.File
import kotlin.system.measureTimeMillis


fun Route.user(service: UserService) {
    route("/users") {
        post("/register") {
            val param = call.receive<UserRegisterParam>()
            var avatar = ""

            val millis = measureTimeMillis {
                avatar = Clients.generateAvatar(param.userName)
            }
            logD(millis)

            val newUser = User(userName = param.userName,
                    password = param.password,
                    avatar = avatar)
            val result = service.addNewUser(newUser)
            result?.apply {
                token = xishiJwt.sign(userId)
            }

            if (result != null) {
                call.respond(HttpStatusCode.Created, Resp(content = result))
            } else {
                call.respond(Resp<Any>(Resp.NOT_FOUNT))
            }
        }

        get("/") {
            val needExtra = call.request.queryParameters["need_extra"]?.toBoolean() ?: false
            val allUsers = service.retrieveAllUsers(needExtra)
            call.respond(Resp(Resp.OK, allUsers))
        }

        jwtAuth {
            post("/edit-name") {
                call.checkUserId { userId ->
                    val param = receive<UpdateNameParam>()
                    val newName = param.newName
                    if (newName.isNotEmpty()) {
                        service.updateUserName(userId, newName)
                        respond(Resp(content = newName))
                    } else {
                        respond(Resp<Any>(Resp.PARAMS_ERROR))
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
                            respond(Resp<Any>(Resp.OK))
                        } else {
                            respond(Resp<Any>(Resp.WRONG_PASSWORD))
                        }
                    } else {
                        respond(Resp<Any>(Resp.PARAMS_ERROR))
                    }
                }
            }
        }
        jwtAuth {
            post("/avatar") {
                val multipart = call.receiveMultipart()
                var mediaFile: File? = null
                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val ext = File(part.originalFileName).extension
                            val uploadDir = File("./")
                            val file = File(uploadDir, "upload-${System.currentTimeMillis()}-${call.userId()}.$ext")
                            part.streamProvider().use { its ->
                                file.outputStream().buffered().use {
                                    its.copyToSuspend(it)
                                }
                            }
                            mediaFile = file
                        }
                    }

                    part.dispose()
                }
                call.respond(Resp(Resp.OK, mediaFile?.name ?: "error"))
            }
        }
    }
}





