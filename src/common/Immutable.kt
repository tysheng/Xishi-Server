package com.tysheng.xishi.server.common

import com.tysheng.xishi.server.auth.XishiJwt
import com.tysheng.xishi.server.repo.XishiServiceImpl

val xishiJwt = XishiJwt()
val xishiService = XishiServiceImpl()
const val SEPARATOR = ";"
const val CLAIM_USER_ID = "userId"

const val JWT = "jwt"
