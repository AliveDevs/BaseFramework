package com.akashic.framework.utils

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 *  author: akashic
 *
 *  date: 2021-01-12 9:51 AM
 *
 *  desc:
 */
object GsonUtil {


    private val gson = GsonBuilder().create()


    fun <T> fromJson(json: String, clz: Class<T>): T = gson.fromJson(json, clz)

    fun <T> fromJson(json: String, type: Type): T = gson.fromJson(json, type)


    fun toJson(obj: Any): String = gson.toJson(obj)

    fun toMap(obj: Any): Map<String, Any> {
        val json = toJson(obj)
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(json, type)
    }
}