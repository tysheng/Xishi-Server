package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot

interface GalleryService {

    suspend fun addAlbum(userId: Int, album: Album): Int
    suspend fun deleteAlbum(userId: Int, albumId: Int): Int
    suspend fun addShot(userId: Int, shot: Shot): Int
    suspend fun deleteShot(userId: Int, shotId: Int): Int

    suspend fun addAlbum(album: Album): Album?
    suspend fun retrieveAlbum(albumId: Int): Album?

    suspend fun addShot(shot: Shot): Shot?
    suspend fun retrieveShot(shotId: Int): Shot?

    suspend fun retrieveShotsByAlbum(albumId: Int): List<Shot>?

    suspend fun allAlbums(userId: Int): List<Album>?
    suspend fun allShots(userId: Int): List<Shot>?
}