package com.tysheng.xishi.server

import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.Logger


private val logger = LoggerFactory.getLogger("XsLog")
fun logD(any: Any) {
    if (any is Throwable) {
        logger.debug("throw is ",any)
    } else {
        logger.debug(any.toString())
    }
}