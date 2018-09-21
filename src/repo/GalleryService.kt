package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot

interface GalleryService {
    suspend fun addAlbum(album: Album): Album?
    suspend fun retrieveAlbum(albumId: Int): Album

    suspend fun addShot(shot: Shot):Shot?
    suspend fun retrieveShot(shotId: Int): Shot

    suspend fun retrieveShotsByAlbum(albumId: Int): List<Shot>
}