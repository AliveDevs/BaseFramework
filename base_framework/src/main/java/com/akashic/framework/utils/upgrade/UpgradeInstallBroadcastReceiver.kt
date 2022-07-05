package com.akashic.framework.utils.upgrade

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.akashic.framework.ext.toast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ServiceUtils
import java.io.File

/**
 * 升级安装通知栏点击事件接收
 */
class UpgradeInstallBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action != "upgrade.install") return
        val file = p1.getSerializableExtra("file") as File?
        if (file == null) {
            toast("安装包不存在")
            return
        }
        ServiceUtils.stopService(UpgradeDownloadService::class.java)
        AppUtils.installApp(file)
    }
}