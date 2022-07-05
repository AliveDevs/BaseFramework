package com.akashic.framework.widgets.span

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class MyLinkSpan(private val url: String, private val onclickCallback: (url:String) -> Unit) :
    ClickableSpan() {

    override fun onClick(p0: View) {
        onclickCallback.invoke(url)
//        if(p0 is TextView){
//            p0.
//        }
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}