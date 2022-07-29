package com.akashic.framework.ext

import android.text.SpannableString
import com.akashic.framework.widgets.span.MyLinkSpan
import com.akashic.framework.utils.GsonUtil
import com.blankj.utilcode.util.TimeUtils

import java.lang.reflect.Type

/**
 *  author: akashic
 *
 *  date: 2021-01-11 11:02 AM
 *
 *  desc: String 扩展
 */

val String.isVideoFile get() = matches(".+(://).+\\.(mp4|wmv|avi|mpeg|rm|rmvb|flv|3gp|mov|mkv|mod|)".toRegex())

/**
 * 反序列化
 */
fun <T> String.asObj(clz: Class<T>): T = GsonUtil.fromJson(this, clz)

/**
 * 反序列化
 */
fun <T> String.asObj(type: Type): T? = GsonUtil.fromJson(this, type)

/**
 * 将手机号格式化成 111***1111 的格式
 */
fun String?.mobileMaskText(): String? {
    if (this?.length != 11) {
        return this
    }
    return this.replaceRange(IntRange(3, 6), "****")
}

fun String?.idCardMaskText(): String? {
    if (this?.length != 18) {
        return this
    }
    return this.replaceRange(IntRange(0, 13), "****")
}


fun String?.friendTime(): String? = TimeUtils.getFriendlyTimeSpanByNow(this)


fun String?.limitLength(length: Int = this?.length ?: 0): String? {
    if (this.isNullOrEmpty() || length < 1 || this.length < length) {
        return this
    }
    return this.dropLast(this.length - length).plus("..")
}


/**
 * 移除尾部多余的0
 */
fun String?.removeExtraZero(): String {
    if (this == null) {
        return "0"
    }
    if (this.indexOf(".") > 0) {
        return this.replace("0+?$".toRegex(), "").replace("[.]$".toRegex(), "")
    }
    return this
}

///转换为可点击链接文字
fun String.withLink(link: String, onTap: (url: String) -> Unit): SpannableString {
    val spannableString = SpannableString(this)
    spannableString.setSpan(MyLinkSpan(link) {
        onTap.invoke(it)
    }, 0, spannableString.length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableString
}
