package com.akashic.framework.ext

import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlin.math.abs

/**
 * 隐藏当前View
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * 显示当前View
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * 隐藏当前View，但是仍占位
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 *  防止重复点击
 *  Author: akatosh
 *  Date : 2019-09-12 17:14
 */
inline fun <T : View> T.singleClick(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}

//兼容点击事件设置为this的情况
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 800) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

/**
 * 给当前View添加一块状态栏高度的占位View,一般用于Activity和Fragment
 */
fun View.addFakeStatusBar(fakeStatusBar: View) {
    when (this) {
        ///如果是下拉刷新布局 获取它的第一个子view
        is SmartRefreshLayout -> {
            getChildAt(0).addFakeStatusBar(fakeStatusBar)
        }
        is LinearLayout -> {
            val lp = LinearLayout.LayoutParams(-1, 0)
            addView(fakeStatusBar, 0, lp)
        }
        is RelativeLayout -> {
            val lp = RelativeLayout.LayoutParams(-1, 0)
            addView(fakeStatusBar, 0, lp)
            ///获取相对布局第一个子布局 将其置于fakeView之下
            if (childCount > 0) {
                val firstView = getChildAt(1)
                firstView.updateLayoutParams<RelativeLayout.LayoutParams> {
                    addRule(RelativeLayout.BELOW, fakeStatusBar.id)
                }
            }

        }
        is ConstraintLayout -> {
            val lp = ConstraintLayout.LayoutParams(-1, 0).apply {
                topToTop = ConstraintSet.PARENT_ID
            }
            addView(fakeStatusBar, 0, lp)

            if (childCount > 0) {
                val firstView = getChildAt(1)

                val constraintSet = ConstraintSet()
                constraintSet.clone(this)
                constraintSet.connect(
                    firstView.id,
                    ConstraintSet.TOP,
                    fakeStatusBar.id,
                    ConstraintSet.BOTTOM
                )
                constraintSet.applyTo(this)
            }
        }
    }
}

/**
 * 根据RecyclerView滑动距离设置透明度
 * @receiver View
 * @param recyclerView RecyclerView
 * @param height Int 用于判断的滑动总距离
 */
fun View.changeAlphaWithRecyclerView(recyclerView:RecyclerView,height:Int){
    var totalScroll = 0
    recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            totalScroll += dy
            alpha = if (totalScroll <= 0) {
                0f
            } else if (totalScroll in 1..height) {
                val a= totalScroll/height.toFloat()
                a
            } else {
                1f
            }
        }
    })
}

fun View.changeAlphaWithAppbarLayout(appBarLayout: AppBarLayout,height: Int){
    appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
        val totalScroll = abs(verticalOffset)
       alpha = if (totalScroll <= 0) {
            0f
        } else if (totalScroll in 1..height) {
            val a= totalScroll/height.toFloat()
            a
        } else {
            1f
        }
    })
}

