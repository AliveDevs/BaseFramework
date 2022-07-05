package com.akashic.framework.ext

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import okio.IOException
import retrofit2.HttpException

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
/**
 *  author: akashic
 *
 *  date: 2021-01-11 11:02 AM
 *
 *  desc: 异常类 扩展
 */

/**
 * 转换错误信息为文字提示
 */
val Throwable.msg: String
    get() {
        return when (this) {
            is UnknownHostException -> "无法解析到服务器"
            is StatusErrorException -> this.tip
            is SocketTimeoutException,
            is TimeoutException,
            is TimeoutCancellationException -> "连接超时,请稍后再试"
            is ConnectException -> "网络不给力，请稍候重试！"
            is HttpException -> this.message?:"网络异常"
            is JsonParseException,
            is JsonSyntaxException ->"数据格式异常"
            is NullPointerException -> "指定数据不存在"
            is CancellationException -> ""
            else ->this.message?:"未知错误"
        }
    }

/**
 * 状态码错误异常
 */
data class StatusErrorException(val tip: String) : IOException(tip)

/**
 * Token错误
 */
data class TokenErrorException(val tip:String):IOException(tip)