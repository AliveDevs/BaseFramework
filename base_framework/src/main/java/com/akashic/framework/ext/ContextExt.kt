package com.akashic.framework.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.akashic.framework.utils.engine.GlideEngine
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.style.PictureSelectorStyle
import com.orhanobut.logger.Logger
import com.permissionx.guolindev.PermissionX
import java.util.ArrayList

/**
 * 获取字符串资源
 */
fun getStr(resId: Int) = if (resId > 0) Utils.getApp().getString(resId) else ""


/**
 * 获取颜色资源
 */
fun getColour(@ColorRes resId: Int) = ContextCompat.getColor(Utils.getApp(), resId)

fun debug(obj: Any?, force: Boolean = false) {
    //如果开启日志开关或者 强制记录 才输出日志
//    if(AppConfig.saveLog || force){
    Logger.d(obj)
//    }
}

fun toast(msg: Int, duration: Int = Toast.LENGTH_SHORT, gravity: Int? = null) {
    if (msg < 1) return
    toast(getStr(msg), duration, gravity)
}

/**
 * 显示提示消息
 */
fun toast(msg: String?, duration: Int = Toast.LENGTH_SHORT, gravity: Int? = null) {
    if (msg.isNullOrEmpty()) return
    if (ActivityUtils.getTopActivity() == null) return
    ActivityUtils.getTopActivity().runOnUiThread {
        Toast.makeText(Utils.getApp().applicationContext, msg, duration)
            .apply {
                if (gravity != null)
                    setGravity(gravity, 0, 0)
            }.show()
    }
}

/**
 * 判断是否拥有某个权限
 */
fun Context.hasPermission(permission: String): Boolean {
    return PermissionChecker.checkSelfPermission(
        this,
        permission
    ) == PermissionChecker.PERMISSION_GRANTED
}


fun FragmentActivity.withPermission(
    vararg permission: String,
    block: (isGranted: Boolean) -> Unit
) {
    PermissionX.init(this).permissions(*permission).request { allGranted, _, _ ->
        block.invoke(allGranted)
    }
}


fun Fragment.withPermission(
    vararg permission: String,
    block: (isGranted: Boolean) -> Unit
) {
    PermissionX.init(this).permissions(*permission).request { allGranted, _, _ ->
        block.invoke(allGranted)
    }
}

fun Activity.selectPic(
    maxSelectNum: Int = 1,
    selectMimeType: Int = SelectMimeType.ofAll(),
    style: PictureSelectorStyle? = null,
    callback: (result: ArrayList<LocalMedia>?) -> Unit
) {
    PictureSelector.create(this)
        .openGallery(selectMimeType)
        .setImageEngine(GlideEngine.createGlideEngine())
        .setMaxSelectNum(maxSelectNum)
        .setSelectorUIStyle(style)
        .forResult(object : OnResultCallbackListener<LocalMedia> {
            override fun onResult(result: ArrayList<LocalMedia>?) {
                callback.invoke(result)
            }

            override fun onCancel() {

            }
        }
        )
}

fun Activity.selectSinglePic(
    selectMimeType: Int = SelectMimeType.ofAll(),
    style: PictureSelectorStyle? = null,
    callback: (result: LocalMedia?) -> Unit
) {
    selectPic(1, selectMimeType, style) {
        if (it.isNullOrEmpty()) {
            callback.invoke(null)
        } else {
            callback.invoke(it[0])
        }
    }
}


fun Fragment.selectPic(
    maxSelectNum: Int = 1,
    selectMimeType: Int = SelectMimeType.ofAll(),
    style: PictureSelectorStyle? = null,
    callback: (result: ArrayList<LocalMedia>?) -> Unit
) {
    this.requireActivity().selectPic(maxSelectNum, selectMimeType, style, callback)
}

fun Fragment.selectSinglePic(
    selectMimeType: Int = SelectMimeType.ofAll(),
    style: PictureSelectorStyle? = null,
    callback: (result: LocalMedia?) -> Unit
) {
    this.requireActivity().selectSinglePic(selectMimeType,style,callback)
}


/**
 * 将路由字符串转换为ARouter路由对象
 */

val String.asRoute: Postcard
    get() = ARouter.getInstance().build(this)

/**
 * ARouter 页面跳转
 */
fun Postcard.go(
    requestCode: Int = -1,
    clear: Boolean = false,
    replace: Boolean = false
) {
    val activity = ActivityUtils.getTopActivity()
    if (clear) withFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    if (activity == null) {
        withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        navigation()
    } else {
        navigation(activity, requestCode, object : NavigationCallback {
            override fun onLost(postcard: Postcard?) {
                toast("未找到相应页面")
            }

            override fun onFound(postcard: Postcard?) {

            }

            override fun onInterrupt(postcard: Postcard?) {

            }

            override fun onArrival(postcard: Postcard?) {
                if (replace) activity.finish()
            }
        })
    }
}

