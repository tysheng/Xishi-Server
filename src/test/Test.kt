package com.tysheng.xishi.server.test

import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random

/**
 * Created by tysheng
 * Date: 5/11/18 12:05 PM.
 * Description:
 */
val parent = Job()
//private val scope = CoroutineScope(parent)
//private val childScope = CoroutineScope(Dispatchers.Main + Job(parent))

fun main(args: Array<String>) = runBlocking {
    for (i in 1..3) {
        //scope.
        launch {
            println("launch ${Thread.currentThread().name}")
            val string = test()
//            println(string)
            val result = /*childScope.*/async {

                delay(500)
                println("async ${Thread.currentThread().name}")
                "123"
            }.await()
            println("result $result")
        }
    }
    println("main ${Thread.currentThread().name}")
//    delay(1000)
}

suspend fun test(): String {
    return suspendCoroutine {
        it.resume(Random.nextInt().toString())
//        it.resumeWithException(Exception("e"))
    }
}