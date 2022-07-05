package com.akashic.framework.ext

import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ScreenUtils

/**
 *  author: akashic
 *
 *  date: 2021-01-11 11:04 AM
 *
 *  desc: 数字类 扩展
 */


val Float.sp2px get() = (this * ScreenUtils.getScreenDensity())

val Float.sp get() = (this / ScreenUtils.getScreenDensity())

val Float.dp get() = (ConvertUtils.dp2px(this)).toFloat()

val Int.sp2px get() = this.toFloat().sp2px.toInt()

val Int.sp get() = this.toFloat().sp.toInt()

val Int.dp get() = (ConvertUtils.dp2px(this.toFloat()))

/**
 * 移除尾部多余的0
 */
fun Double?.removeExtraZero(): String {
    if (this == null) {
        return "0"
    }
    val doubleStr = this.toString()
    if (doubleStr.indexOf(".") > 0) {
        return doubleStr.replace("0+?$".toRegex(), "").replace("[.]$".toRegex(), "")
    }
    return doubleStr
}
