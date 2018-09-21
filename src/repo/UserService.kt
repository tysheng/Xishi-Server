package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot
import com.tysheng.xishi.server.data.User

interface UserService {
    suspend fun addNewUser(user: User): User?
    suspend fun updateUser(user: User): User?
    suspend fun retrieveUser(userId: Int): User?
    suspend fun addAlbum(userId: Int, album: Album): Int
    suspend fun deleteAlbum(userId: Int, albumId: Int): Int
    suspend fun addShot(userId: Int, shot: Shot): Int
    suspend fun deleteShot(userId: Int, shotId: Int): Int
}