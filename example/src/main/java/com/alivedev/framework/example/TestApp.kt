package com.alivedev.framework.example

import com.akashic.framework.BaseApplication
import com.core.result.ActivityResultApi
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TestApp:BaseApplication() {

    override val logTag: String
        get() = "TestApp"
    override val isDebug: Boolean
        get() = true

    override fun onCreate() {
        super.onCreate()

        ActivityResultApi.init(this)
    }
}