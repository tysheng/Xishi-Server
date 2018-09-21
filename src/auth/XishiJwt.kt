package com.tysheng.xishi.server.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.tysheng.xishi.server.common.CLAIM_USER_ID


class XishiJwt(secret: String = "xishi-jwt-secret") {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).build()
    fun sign(userId: Int): String {
        return JWT.create().withClaim(CLAIM_USER_ID, userId).sign(algorithm)
    }
}