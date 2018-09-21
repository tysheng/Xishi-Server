package com.tysheng.xishi.server

import org.slf4j.LoggerFactory


private val logger = LoggerFactory.getLogger("XsLog")
fun logD(any: Any?) {
    when (any) {
        null -> {
            logger.debug("is null")
        }
        is Throwable -> logger.debug("throw is ", any)
        else -> logger.debug(any.toString())
    }
}