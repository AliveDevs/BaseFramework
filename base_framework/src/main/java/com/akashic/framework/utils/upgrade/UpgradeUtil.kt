package com.akashic.framework.utils.upgrade

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.FragmentActivity
import com.akashic.framework.ext.toast
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ServiceUtils

object UpgradeUtil {


    /**
     * 开始检测是否需要更新
     * @param fragmentActivity FragmentActivity
     * @param iconResId Int
     * @param newVersionCode Int
     * @param newVersionName String
     * @param upgradeContent String
     * @param downloadUrl String
     * @param isForceUpgrade Boolean
     * @param fromUserClick Boolean
     * @param activeColor Int
     */
    fun startCheck(
        fragmentActivity: FragmentActivity,
        iconResId: Int,
        newVersionCode: Int,
        newVersionName: String,
        upgradeContent: String,
        downloadUrl: String,
        isForceUpgrade: Boolean,
        fromUserClick: Boolean = false,
        activeColor: Int = Color.RED
    ) {
        if (ServiceUtils.isServiceRunning(UpgradeDownloadService::class.java)) {
            toast("正在下载更新")
            return
        }
        val currentVersionCode = AppUtils.getAppVersionCode()
        //当前版本号小于等于检测到的版本号 说明无更新
        if (newVersionCode <= currentVersionCode) {
            if (fromUserClick)
                toast("已经是最新版本")
            return
        }
        UpgradeDialog(fragmentActivity).apply {
            setLogo(iconResId)
            setActiveColor(activeColor)
            setNewVersionName(newVersionName)
            setUpgradeContent(upgradeContent)
            setDownloadLink(downloadUrl)
            setIsForceUpgrade(isForceUpgrade)
        }.show()
    }

    /**
     * 清除升级残留的安装包
     */
    fun clear(context: Context) {
        val dir = context.getExternalFilesDir(null)?.absolutePath!!
        FileUtils.deleteFilesInDirWithFilter(dir) { it.name.endsWith(".apk") }
    }
}