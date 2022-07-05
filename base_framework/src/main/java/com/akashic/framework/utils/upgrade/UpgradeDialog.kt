package com.akashic.framework.utils.upgrade

import android.Manifest
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.akashic.framework.databinding.UpgradeDialogBinding
import com.akashic.framework.ext.*
import com.akashic.framework.widgets.dialog.BaseBindingDialog
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.IntentUtils
import com.blankj.utilcode.util.ServiceUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.permissionx.guolindev.PermissionX

/**
 * 升级对话框
 */
class UpgradeDialog(private val activity: FragmentActivity) :
    BaseBindingDialog<UpgradeDialogBinding>(activity) {

    /**
     * logo资源ID
     */
    private var logoResId = -1

    /**
     * 新版本名称
     */
    private var newVersionName = ""

    /**
     * 更新内容
     */
    private var upgradeContent: CharSequence? = null

    /**
     * 安装包下载地址
     */
    private var downloadUrl = ""

    /**
     * 是否是强制更新
     */
    private var isForceUpgrade = false

    /**
     * 高亮背景色
     */
    private var activeBgColor = Color.RED

    /**
     * 更新按钮点击回调
     */
    private var onUpgradeButtonClickListener: DialogInterface.OnClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCancelable(!isForceUpgrade)
        setCanceledOnTouchOutside(!isForceUpgrade)

        binding.mainLayout.background = shape {
            corners(10f.dp)
            solid(Color.parseColor("#f2f2f2"))
        }


        binding.logo.isVisible = logoResId > 0
        if (logoResId > 0) {
            binding.logo.bind(logoResId)
        }

        binding.newVersion.text = newVersionName

        binding.upgradeContent.text = upgradeContent ?: "暂无更新内容"

        binding.upgradeBtn.background = shape {
            corners(36f.dp)
            solid(activeBgColor)
        }

        binding.upgradeBtn.singleClick {
            if (onUpgradeButtonClickListener != null) {
                onUpgradeButtonClickListener?.onClick(this, 0)
                return@singleClick
            }
            PermissionX.init(activity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request { allGranted, _, _ ->
                    if (!allGranted) {
                        val builder = AlertDialog.Builder(activity).setMessage("你拒绝了存储权限，是否去重新授予？")
                        builder.setNegativeButton("取消") { dialog, _ ->
                            dialog.dismiss()
                            if (!isForceUpgrade) {
                                dismiss()
                            }
                        }
                        builder.setPositiveButton("") { dialog, _ ->
                            dialog.dismiss()
                            activity.startActivity(
                                IntentUtils.getLaunchAppDetailsSettingsIntent(
                                    context.packageName
                                )
                            )
                        }
                        builder.show()
                        return@request
                    }
                    startUpgrade()
                }
        }

        binding.closeBtn.isVisible = !isForceUpgrade
        binding.closeBtn.singleClick {
            dismiss()
        }

        LiveEventBus.get<DownloadProgressEvent>(UpgradeDownloadService.DOWNLOAD_EVENT)
            .observeForever { event ->
                if (!isForceUpgrade) return@observeForever
                if (event.progress < 100) {
                    binding.upgradeBtn.text = "正在下载${event.progress}%"
                    binding.upgradeBtn.singleClick { }
                } else {
                    ServiceUtils.startService(UpgradeDownloadService::class.java)
                    binding.upgradeBtn.text = "立即安装"
                    binding.upgradeBtn.singleClick {
                        AppUtils.installApp(event.file)
                    }
                }
            }
    }

    private fun startUpgrade() {
        if (downloadUrl.isEmpty() || downloadUrl.endsWith(".apk")) {
            toast("下载地址错误")
            dismiss()
            return
        }
        if (!isForceUpgrade) {
            dismiss()
        } else {
            binding.upgradeBtn.singleClick { }

        }
        UpgradeDownloadService.startDownload(
            context.applicationContext,
            downloadUrl,
            logoResId,
            isForceUpgrade
        )

    }


    /**
     * 设置Logo
     * @param resId Int logo资源ID
     */
    fun setLogo(resId: Int) {
        logoResId = resId
    }

    /**
     * 设置新版本名称
     * @param versionName String 版本名称
     */
    fun setNewVersionName(versionName: String) {
        newVersionName = versionName
    }

    /**
     * 设置更新内容
     * @param content String 更新内容
     */
    fun setUpgradeContent(content: String) {
        upgradeContent = content
    }


    /**
     * 是否强制更新
     */
    fun setIsForceUpgrade(force: Boolean) {
        isForceUpgrade = force
    }

    /**
     * 设置下载地址
     * @param url String 下载链接
     */
    fun setDownloadLink(url: String) {
        downloadUrl = url
    }


    /**
     * 设置统一按钮背景色
     * @param color Int 背景色值
     */
    fun setActiveColor(color: Int) {
        activeBgColor = color
    }

    /**
     * 设置更新按钮点击事件
     */
    fun setUpgradeButtonListener(onClickListener: DialogInterface.OnClickListener) {
        onUpgradeButtonClickListener = onClickListener
    }
}