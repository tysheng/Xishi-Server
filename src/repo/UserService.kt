package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.Album
import com.tysheng.xishi.server.data.Shot
import com.tysheng.xishi.server.data.User

interface UserService {
    suspend fun addNewUser(user: User): User?
    suspend fun updateUser(user: User): User?
    /**
     * @param needExtra extra info, e.g. albums, shots
     */
    suspend fun retrieveUser(userId: Int, needExtra: Boolean = true): User?

    suspend fun updateUserName(userId: Int, newName: String)

    suspend fun addAlbum(userId: Int, album: Album): Int
    suspend fun deleteAlbum(userId: Int, albumId: Int): Int
    suspend fun addShot(userId: Int, shot: Shot): Int
    suspend fun deleteShot(userId: Int, shotId: Int): Int
}