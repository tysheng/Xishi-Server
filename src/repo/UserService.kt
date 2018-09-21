package com.tysheng.xishi.server.repo

import com.tysheng.xishi.server.data.User

interface UserService {
    suspend fun addNewUser(user: User): User?
    /**
     * @param needExtra extra info, e.g. albums, shots
     */
    suspend fun retrieveUser(userId: Int, needExtra: Boolean = true): User?

    suspend fun updateUserName(userId: Int, newName: String)
    suspend fun updatePassword(userId: Int, oldPsw: String, newPsw: String): UpdatePasswordServiceResult

    suspend fun allUsers(needExtra: Boolean = true): List<User>?
}