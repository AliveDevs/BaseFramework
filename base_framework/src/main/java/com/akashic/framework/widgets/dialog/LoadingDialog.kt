package com.akashic.framework.widgets.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.akashic.framework.ext.dp
import com.akashic.framework.ext.shape
import com.scwang.smart.drawable.ProgressDrawable

/**
 * 加载对话框 用于阻断用户操作
 */
class LoadingDialog(context: Context) : BaseDialog(context) {

    private lateinit var loadingImageview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingImageview = ImageView(context).apply {
            setImageDrawable(ProgressDrawable().apply {
                setColor(-0x99999a)
            })
        }

        val rootView = LinearLayout(context).apply {
            background = shape {
                corners(8f.dp)
                solid(Color.parseColor("#55000000"))
            }
            setPadding(10.dp)
            orientation = LinearLayout.VERTICAL
            addView(loadingImageview)
        }
        setContentView(rootView)

        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (loadingImageview.drawable as ProgressDrawable).start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (loadingImageview.drawable as ProgressDrawable).stop()
    }
}