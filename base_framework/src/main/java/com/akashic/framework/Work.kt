package com.akashic.framework

import kotlinx.coroutines.CoroutineScope

/**
 *  author: akashic
 *
 *  date: 2021-03-13 9:59 PM
 *
 *  desc: 协程 dsl
 */
class Work {
    lateinit var funcWork: suspend CoroutineScope.() -> Unit
    var funcOnStart: () -> Boolean = { false }
    var funcOnError: (Throwable) -> Boolean = { false }
    var funcOnFinally: () -> Boolean = { false }


    fun work(work: suspend CoroutineScope.() -> Unit) {
        funcWork = work
    }


    fun onStart(onStart: () -> Boolean) {
        funcOnStart = onStart
    }

    fun onError(onError: (Throwable) -> Boolean) {
        funcOnError = onError
    }

    fun onFinally(onFinally: () -> Boolean) {
        funcOnFinally = onFinally
    }

}