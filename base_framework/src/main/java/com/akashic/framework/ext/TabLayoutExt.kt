package com.akashic.framework.ext

import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ReflectUtils
import com.flyco.tablayout.SlidingTabLayout

/**
 * 修复首次绑定后 默认选中的tab样式没更新的bug
 */
fun SlidingTabLayout.bindViewPager(viewPager: ViewPager){
    setViewPager(viewPager)
    ReflectUtils.reflect(this).method("updateTabSelection", 0)
}