package com.akashic.framework.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.core.widget.TextViewCompat
import com.akashic.framework.R

/**
 * Title居中的Toolbar
 */
class CenterTitleToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    private var mTitleTextAppearance = 0
    private var mTitleTextColor: ColorStateList? = null
    private var mTitleTextView: TextView? = null
    private var mTitleText: CharSequence? = null

    init {

        val ta = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.CenterTitleToolbar,
            defStyleAttr,
            0
        )
        mTitleTextAppearance =
            ta.getResourceId(R.styleable.CenterTitleToolbar_ctt_titleTextAppearance, 0)
        mTitleText = ta.getText(R.styleable.CenterTitleToolbar_ctt_title)
        title = if (!TextUtils.isEmpty(mTitleText)) {
            mTitleText
        }else{
            ""
        }
        if (ta.hasValue(R.styleable.CenterTitleToolbar_ctt_titleTextColor)) {
            setTitleTextColor(ta.getColor(R.styleable.CenterTitleToolbar_ctt_titleTextColor, 0))
        }

        if (ta.hasValue(R.styleable.CenterTitleToolbar_ctt_naviIconColor)) {
            val naviColor = ta.getColor(R.styleable.CenterTitleToolbar_ctt_naviIconColor, 0)
            children.forEach {
                if(it is ImageButton){
                    it.setColorFilter(naviColor)
                }
            }

        }

        ta.recycle()


    }

    override fun setTitle(title: CharSequence?) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                mTitleTextView = AppCompatTextView(context).apply {
                    isSingleLine = true
                    ellipsize = TextUtils.TruncateAt.END
                    addView(
                        this,
                        LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                        )
                    )
                }
            }
            if (mTitleTextAppearance != 0) {
                mTitleTextView?.apply {
                    TextViewCompat.setTextAppearance(this, mTitleTextAppearance)
                }
            }
            if (mTitleTextColor != null) {
                mTitleTextView?.setTextColor(mTitleTextColor)
            }
        } else if (mTitleTextView != null) {
            removeView(mTitleTextView)
        }
        mTitleTextView?.text = title
        mTitleText = title
    }

    override fun getTitle(): CharSequence? {
        return mTitleText
    }

    override fun setTitleTextColor(@NonNull color: ColorStateList) {
        mTitleTextColor = color
        mTitleTextView?.setTextColor(color)
    }

    override fun setTitleTextAppearance(context: Context, @StyleRes resId: Int) {
        mTitleTextAppearance = resId
        mTitleTextView?.apply {
            TextViewCompat.setTextAppearance(this, resId)
        }
    }
}