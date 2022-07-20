package com.akashic.framework.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.os.CountDownTimer
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.akashic.framework.ext.singleClick

class CaptchaButton : AppCompatTextView {


    private var timer: CountDownTimer? = null

    private var defaultText:String = ""

    private var countDownFormatStr=  "%ds"

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun init(
        textStr: String,
        enableTextColor: Int,
        disableTextColor: Int,
        countDownFormat:String = "%ds",
        requestFunc: (() -> Unit)
    ) {
        defaultText = textStr
        countDownFormatStr = countDownFormat
        text = textStr
        setTextColor(
            ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_enabled),
                    intArrayOf(-android.R.attr.state_enabled)
                ),
                intArrayOf(
                    enableTextColor,
                    disableTextColor
                )
            )
        )
        singleClick {
            if (!isEnabled) return@singleClick
            requestFunc.invoke()
        }
    }

    fun startCountDown(totalTime: Int = 60) {
        if (!isEnabled) return
        if (timer == null)
            timer = object : CountDownTimer(totalTime * 1000L, 1000) {
                override fun onTick(p0: Long) {
                    isEnabled = false
                    text = String.format(countDownFormatStr, p0 / 1000)
                }

                override fun onFinish() {
                    isEnabled = true
                    text = defaultText
                }

            }
        timer?.start()
    }

    override fun onDetachedFromWindow() {
        timer?.cancel()
        timer = null
        super.onDetachedFromWindow()
    }
}