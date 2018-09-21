package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot
import com.tysheng.xishi.server.repo.GalleryService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route

fun Route.gallery(service: GalleryService) {
    route("/album") {
        jwtAuth {
            post("/add") {
                call.checkUserId { userId ->
                    val param = receive<AddAlbumParam>()
                    service.addAlbum(userId, Album(albumId = param.albumId,
                            title = param.title,
                            url = param.url,
                            addTime = param.addTime))
                    respond(CommonResponse<Any>(CommonResponse.OK))
                }
            }
        }
        jwtAuth {
            post("/delete") {
                call.checkUserId { userId ->
                    val param = receive<DeleteParam>()
                    service.deleteAlbum(userId, param.id)
                    respond(CommonResponse<Any>(CommonResponse.OK))
                }
            }
        }
        jwtAuth {
            get("/all") {
                call.checkUserId { userId ->
                    val albums = service.allAlbums(userId)
                    respond(CommonResponse(CommonResponse.OK, albums))
                }
            }
        }
        get("/{id}") {
            var albumId: Int? = null
            try {
                albumId = call.parameters["id"]?.toInt()
            } catch (e: NumberFormatException) {
                call.respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
            }
            if (albumId != null) {
                val album = service.retrieveAlbum(albumId)
                call.respond(CommonResponse(CommonResponse.OK, album))
            }
        }
    }


    route("/shot") {
        jwtAuth {
            post("/add") {
                call.checkUserId { userId ->
                    val param = receive<AddShotParam>()
                    service.addShot(userId, Shot(
                            shotId = param.shotId,
                            albumId = param.albumId,
                            title = param.title,
                            url = param.url,
                            addTime = param.addTime,
                            thumb = param.thumb,
                            youShotLink = param.youShotLink,
                            imageSize = param.imageSize,
                            author = param.author,
                            content = param.content))
                    respond(CommonResponse<Any>(CommonResponse.OK))
                }
            }
        }
        jwtAuth {
            post("/delete") {
                call.checkUserId { userId ->
                    val param = receive<DeleteParam>()
                    service.deleteShot(userId, param.id)
                    respond(CommonResponse<Any>(CommonResponse.OK))
                }
            }
        }
        jwtAuth {
            get("/all") {
                call.checkUserId { userId ->
                    val shots = service.allShots(userId)
                    respond(CommonResponse(CommonResponse.OK, shots))
                }
            }
        }
        get("/{id}") {
            var shotId: Int? = null
            try {
                shotId = call.parameters["id"]?.toInt()
            } catch (e: NumberFormatException) {
                call.respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
            }
            if (shotId != null) {
                val shot = service.retrieveShot(shotId)
                call.respond(CommonResponse(CommonResponse.OK, shot))
            }
        }

    }

    /**
     * shots by album id
     */
    get("/shots/{id}") {
        var albumIdForShots: Int? = null
        try {
            albumIdForShots = call.parameters["id"]?.toInt()
        } catch (e: NumberFormatException) {
            call.respond(CommonResponse<Any>(CommonResponse.PARAMS_ERROR))
        }
        if (albumIdForShots != null) {
            val shots = service.retrieveShotsByAlbum(albumIdForShots)
            call.respond(CommonResponse(CommonResponse.OK, shots))
        }
    }

}