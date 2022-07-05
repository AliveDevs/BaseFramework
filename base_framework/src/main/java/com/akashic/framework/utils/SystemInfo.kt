package com.akashic.framework.utils

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.provider.Settings
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.Utils
import java.util.*

object SystemInfo {

    val uuid get() = getUUID()

    val deviceId get() = getAndroidId()

    val isSimulator get() = checkIsSimulator()

    private fun getUUID(): String {

        val sb = StringBuilder()
        val androidId = getAndroidId()
        val deviceUUID = getDeviceUUID()

        if (androidId.isNotEmpty()) {
            sb.append(androidId)
            sb.append("|")
        }

        if (deviceUUID.isNotEmpty()) {
            sb.append(deviceUUID)
        }


        return EncryptUtils.encryptSHA1ToString(sb.toString())
    }


    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return try {
            Settings.Secure.getString(
                Utils.getApp().contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (e: Exception) {
            ""
        }
    }


    private fun getDeviceUUID(): String {
        val androidId = getAndroidId()
        val deviceId = "9527" + Build.ID +
                Build.DEVICE +
                Build.BOARD +
                Build.BRAND +
                Build.HARDWARE +
                Build.PRODUCT +
                Build.MODEL +
                androidId
        return UUID(deviceId.hashCode().toLong(), androidId.hashCode().toLong()).toString()
            .replace("-", "")
    }


    /**
     * 判断是否是模拟器  模拟器一般没有计步器传感器
     */
    private fun checkIsSimulator(): Boolean {
        val s =
            Utils.getApp().applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = s.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
//        val stepDetectorSensor = s.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        return stepCounterSensor == null
    }
}