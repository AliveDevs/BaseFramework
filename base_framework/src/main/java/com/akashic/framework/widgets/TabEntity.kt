package com.akashic.framework.widgets

import androidx.annotation.DrawableRes
import com.flyco.tablayout.listener.CustomTabEntity

/**
 * 首页 tab
 * @property title String tab标题
 * @property iconRes Int 默认tab图标
 * @property selectedIconRes Int 选中的tab图标
 */
class TabEntity(
    private val title: String,
    @DrawableRes private val iconRes: Int,
    @DrawableRes private val selectedIconRes: Int
) : CustomTabEntity {

    override fun getTabTitle() = title

    override fun getTabSelectedIcon() = selectedIconRes

    override fun getTabUnselectedIcon() = iconRes
}