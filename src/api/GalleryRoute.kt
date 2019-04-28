package com.tysheng.xishi.server.api

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot
import com.tysheng.xishi.server.repo.GalleryService
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*

fun Route.gallery(service: GalleryService) {
    route("/albums") {
        jwtAuth {
            post {
                call.checkUserId { userId ->
                    val param = receive<AddAlbumParam>()
                    service.addAlbum(userId, Album(albumId = param.albumId,
                            title = param.title,
                            url = param.url,
                            addTime = param.addTime))
                    respond(Resp<Any>(Resp.OK))
                }
            }
        }
        jwtAuth {
            delete {
                call.checkUserId { userId ->
                    val param = receive<DeleteParam>()
                    service.deleteAlbum(userId, param.id)
                    respond(Resp<Any>(Resp.OK))
                }
            }
        }
        jwtAuth {
            get {
                call.checkUserId { userId ->
                    val albums = service.retrieveAllAlbums(userId)
                    respond(Resp(Resp.OK, albums))
                }
            }
        }
        get("/{id}") {
            var albumId: Int? = null
            try {
                albumId = call.parameters["id"]?.toInt()
            } catch (e: NumberFormatException) {
                call.respond(Resp<Any>(Resp.PARAMS_ERROR))
            }
            if (albumId != null) {
                val album = service.retrieveAlbum(albumId)
                call.respond(Resp(Resp.OK, album))
            }
        }
    }


    route("/shots") {
        jwtAuth {
            post {
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
                    respond(Resp<Any>(Resp.OK))
                }
            }
        }
        jwtAuth {
            delete {
                call.checkUserId { userId ->
                    val param = receive<DeleteParam>()
                    service.deleteShot(userId, param.id)
                    respond(Resp<Any>(Resp.OK))
                }
            }
        }
        jwtAuth {
            get {
                call.checkUserId { userId ->
                    val shots = service.retrieveAllShots(userId)
                    respond(Resp(Resp.OK, shots))
                }
            }
        }
        get("/{id}") {
            var shotId: Int? = null
            try {
                shotId = call.parameters["id"]?.toInt()
            } catch (e: NumberFormatException) {
                call.respond(Resp<Any>(Resp.PARAMS_ERROR))
            }
            if (shotId != null) {
                val shot = service.retrieveShot(shotId)
                call.respond(Resp(Resp.OK, shot))
            }
        }

    }

    /**
     * shots by album id
     */
    get("/album-shots/{id}") {
        var albumIdForShots: Int? = null
        try {
            albumIdForShots = call.parameters["id"]?.toInt()
        } catch (e: NumberFormatException) {
            call.respond(Resp<Any>(Resp.PARAMS_ERROR))
        }
        if (albumIdForShots != null) {
            val shots = service.retrieveShotsByAlbum(albumIdForShots)
            call.respond(Resp(Resp.OK, shots))
        }
    }

}