package com.akashic.framework.utils.upgrade

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.akashic.framework.ext.debug
import com.akashic.framework.ext.toast
import com.blankj.utilcode.util.NotificationUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener1
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist
import kotlin.properties.Delegates

/**
 * 下载新版本安装包的前台服务
 */
class UpgradeDownloadService : Service() {


    companion object {

        private const val CHANNEL_ID = "10111"

        private var CHANNEL_NAME = "upgrade service"

        private const val JOB_ID = 10111

        const val DOWNLOAD_EVENT = "download_upgrade_event"

        fun startDownload(context: Context, url: String, iconResId: Int, isForce: Boolean) {
            val i = Intent(context, UpgradeDownloadService::class.java)
            i.putExtra("url", url)
            i.putExtra("iconResId", iconResId)
            i.putExtra("isForce", isForce)
            context.startService(i)
        }

    }


    private var iconResId by Delegates.notNull<Int>()

    private lateinit var url: String

    private var isForce = false

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        url = intent?.getStringExtra("url") ?: ""
        iconResId = intent?.getIntExtra("iconResId", -1) ?: -1
        isForce = intent?.getBooleanExtra("isForce", false) ?: false

        if (url.isEmpty() || iconResId < 1) {
            toast("下载更新失败")
        } else {
            refreshNotify(0, startForeground = true)
            download()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun refreshNotify(
        progress: Int,
        title: String = "正在下载更新",
        startForeground: Boolean = false,
        pendingIntent: PendingIntent? = null
    ) {
        NotificationUtils.notify(
            JOB_ID, NotificationUtils.ChannelConfig(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationUtils.IMPORTANCE_DEFAULT
            )
        ) {
            it.setContentTitle(title)
                .setOnlyAlertOnce(true)
                .setProgress(100, progress, false)
                .setSmallIcon(iconResId)
                .setAutoCancel(true)
            if (pendingIntent != null) {
                it.setFullScreenIntent(pendingIntent, true)
            }
            if (startForeground)
                startForeground(JOB_ID, it.build())
        }
    }


    private fun download() {

        val fileName = "newVersion_${System.currentTimeMillis() / 1000}.apk"

        val task =
            DownloadTask.Builder(
                url,
                getExternalFilesDir(null)?.absolutePath!!,
                fileName
            ).setConnectionCount(1)
                .setMinIntervalMillisCallbackProcess(30).build()
        task.execute(object : DownloadListener1() {
            override fun taskStart(task: DownloadTask, model: Listener1Assist.Listener1Model) {
                debug("开始下载更新")
                toast("开始下载更新")
            }

            override fun taskEnd(
                task: DownloadTask,
                cause: EndCause,
                realCause: Exception?,
                model: Listener1Assist.Listener1Model
            ) {

            }

            override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
                val progress = ((currentOffset / totalLength.toDouble()) * 100).toInt()
                debug("下载进度 $progress")
                refreshNotify(progress)
                if (isForce) {//是强制更新的 要发送liveBus
                    LiveEventBus.get<DownloadProgressEvent>(DOWNLOAD_EVENT)
                        .post(DownloadProgressEvent(task.file, progress))
                }
                if (progress >= 100) {
                    toast("下载完成")
                    NotificationUtils.cancel(JOB_ID)
                    if (!isForce) {//不是强制更新就刷新通知栏通知一下
                        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        } else {
                            PendingIntent.FLAG_UPDATE_CURRENT
                        }

                        refreshNotify(
                            100, "下载完成，点击安装", pendingIntent = PendingIntent.getBroadcast(
                                applicationContext,
                                1,
                                Intent(
                                    applicationContext,
                                    UpgradeInstallBroadcastReceiver::class.java
                                ).apply {
                                    putExtra("file", task.file)
                                },
                                flag
                            )
                        )
                    }
//                    ThreadUtils.getMainHandler().postDelayed({
//                        AppUtils.installApp(task.file)
//                    }, 1000)
                }
            }

            override fun connected(
                task: DownloadTask,
                blockCount: Int,
                currentOffset: Long,
                totalLength: Long
            ) {

            }

            override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

            }
        })
    }
}