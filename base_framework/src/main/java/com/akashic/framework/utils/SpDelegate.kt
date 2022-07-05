package com.akashic.framework.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.akashic.framework.ext.asObj
import com.akashic.framework.ext.toJsonStr
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.Utils
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 *  author: akashic
 *
 *  date: 2021-03-14 3:57 PM
 *
 *  desc:
 */

open class SpDelegate {


    val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(Utils.getApp().applicationContext) }

    /**
     * 内存缓存
     */
    val cacheMap = hashMapOf<String, Any>()


    fun int(defaultValue: Int = 0) = object : ReadWriteProperty<SpDelegate, Int> {

        override fun getValue(thisRef: SpDelegate, property: KProperty<*>): Int {
            return cacheMap[property.name] as Int? ?: preferences.getInt(
                property.name,
                defaultValue
            )
        }

        override fun setValue(thisRef: SpDelegate, property: KProperty<*>, value: Int) {
            preferences.edit().putInt(property.name, value).apply()
            cacheMap[property.name] = value
        }
    }

    //
    fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<SpDelegate, Long> {

        override fun getValue(thisRef: SpDelegate, property: KProperty<*>): Long {
            return cacheMap[property.name] as Long? ?: preferences.getLong(
                property.name,
                defaultValue
            )
        }

        override fun setValue(thisRef: SpDelegate, property: KProperty<*>, value: Long) {
            preferences.edit().putLong(property.name, value).apply()
            cacheMap[property.name] = value
        }
    }

    //
    fun boolean(defaultValue: Boolean = false) =
        object : ReadWriteProperty<SpDelegate, Boolean> {
            override fun getValue(thisRef: SpDelegate, property: KProperty<*>): Boolean {
                return cacheMap[property.name] as Boolean? ?: preferences.getBoolean(
                    property.name,
                    defaultValue
                )
            }

            override fun setValue(
                thisRef: SpDelegate,
                property: KProperty<*>,
                value: Boolean
            ) {
                preferences.edit().putBoolean(property.name, value).apply()
                cacheMap[property.name] = value
            }
        }

    fun float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<SpDelegate, Float> {
        override fun getValue(thisRef: SpDelegate, property: KProperty<*>): Float {
            return cacheMap[property.name] as Float? ?: preferences.getFloat(
                property.name,
                defaultValue
            )
        }

        override fun setValue(thisRef: SpDelegate, property: KProperty<*>, value: Float) {
            preferences.edit().putFloat(property.name, value).apply()
            cacheMap[property.name] = value
        }
    }

    fun string(defaultValue: String = "") = object : ReadWriteProperty<SpDelegate, String> {

        override fun getValue(thisRef: SpDelegate, property: KProperty<*>): String {
            val src = cacheMap[property.name] as String? ?: preferences.getString(
                property.name,
                defaultValue
            )
            return String(EncodeUtils.base64Decode(src))
        }

        override fun setValue(thisRef: SpDelegate, property: KProperty<*>, value: String) {
            val result = EncodeUtils.base64Encode2String(value.toByteArray())
            preferences.edit()
                .putString(property.name, result)
                .apply()
            cacheMap[property.name] = result
        }
    }

    inline fun <reified T : Any> obj() = object : ReadWriteProperty<SpDelegate, T?> {

        override fun getValue(thisRef: SpDelegate, property: KProperty<*>): T? {
            val src = cacheMap[property.name] as String? ?: preferences.getString(
                property.name,
                ""
            )
            if(src.isNullOrEmpty()){
                return null
            }
            return src.asObj(T::class.java)
        }

        override fun setValue(thisRef: SpDelegate, property: KProperty<*>, value: T?) {
            val str = value?.toJsonStr()
            val result = EncodeUtils.base64Encode2String(str?.toByteArray())
            preferences.edit()
                .putString(property.name, result)
                .apply()
            cacheMap[property.name] = result
        }
    }

}