package com.akashic.framework.widgets.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.akashic.framework.ext.dp
import com.akashic.framework.ext.shape

/**
 * 加载对话框 用于阻断用户操作
 */
class LoadingDialog(context: Context) : BaseDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootView = FrameLayout(context).apply {
            background = shape {
                corners(8f.dp)
                solid(Color.parseColor("#55000000"))
            }
            setPadding(10.dp)
            addView(TextView(context).apply {
                text = "加载中"
            })
        }
        setContentView(rootView)

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}