package com.akashic.framework

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.Utils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

abstract class BaseApplication : Application() {

    /**
     * 输出日志tag
     */
    abstract val logTag: String

    override fun onCreate() {
        super.onCreate()

        Utils.init(this)

        //统一设置刷新控件的header footer
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }

        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
            Logger.addLogAdapter(
                AndroidLogAdapter(
                    PrettyFormatStrategy.newBuilder().methodCount(10).tag(logTag).build()
                )
            )
        }
        ARouter.init(this)
    }

}