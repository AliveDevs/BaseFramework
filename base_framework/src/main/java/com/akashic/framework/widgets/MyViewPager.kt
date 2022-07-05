package com.akashic.framework.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

class MyViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var canScroll = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (canScroll) super.onInterceptTouchEvent(ev) else canScroll
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return if (canScroll) super.onTouchEvent(ev) else true
    }
}

fun ViewPager.bind(
    fragmentManager: FragmentManager,
    fragmentList: ArrayList<Fragment>,
    titleList: ArrayList<String> = arrayListOf()
) {
    this.adapter = object : FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
        override fun getCount() = fragmentList.size

        override fun getItem(position: Int) = fragmentList[position]

        override fun getPageTitle(position: Int) =
            if (titleList.size != fragmentList.size) null else titleList[position]
    }
    offscreenPageLimit = fragmentList.size
}