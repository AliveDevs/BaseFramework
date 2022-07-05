package com.akashic.framework.ext

import com.akashic.framework.utils.GsonUtil

/**
 *  author: akashic
 *
 *  date: 2021-01-11 10:57 AM
 *
 *  desc: 任意类扩展
 */


/**
 * 序列化
 */
fun Any.toJsonStr(): String = GsonUtil.toJson(this)


/**
 * 转成 map
 */
fun Any.toMap(): Map<String, Any> = GsonUtil.toMap(this)
