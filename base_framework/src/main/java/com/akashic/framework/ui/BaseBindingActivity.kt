package com.akashic.framework.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.viewbinding.ViewBinding
import com.akashic.framework.R
import com.akashic.framework.ext.addFakeStatusBar
import com.alibaba.android.arouter.launcher.ARouter
import com.dylanc.viewbinding.base.ViewBindingUtil.inflateWithGeneric
import com.gyf.immersionbar.ImmersionBar

/**
 * 基类
 */
abstract class BaseBindingActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    ///是否使用ARouter注入
    open val injectByARouter = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (injectByARouter)
            ARouter.getInstance().inject(this)
        binding = inflateWithGeneric(this, layoutInflater)
        setContentView(binding.root)

        ImmersionBar.with(this).transparentStatusBar().apply {
            if (provideStatusBar() != null) {
                statusBarView(provideStatusBar())
            } else
                if (provideTitleBar() != null) {
                    titleBar(provideTitleBar())
                }
            setStatusBarDarkFont(true)
        }.init()

        if (provideTitleBar() != null && provideTitleBar() is Toolbar) {
            setSupportActionBar(provideTitleBar() as Toolbar)
        }
    }


    /**
     * 设置状态栏字体颜色
     */
    protected fun setStatusBarDarkFont(useDark: Boolean) {
        ImmersionBar.with(this).apply {
            statusBarDarkFont(useDark)
        }.init()
    }


    /**
     * 获取标题栏
     */
    open fun provideTitleBar(): View? {
        val rootView = binding.root as ViewGroup
        if (rootView.childCount > 0) {
            if (rootView.getChildAt(0) is Toolbar) {
                return rootView[0]
            }
        }
        return null
    }

    /**
     * 设置标题栏标题
     */
    protected fun setToolbarTitle(title: String) {
        val v = provideTitleBar()
        if (v is Toolbar) {
            v.title = title
        }
    }

    /**
     * 设置标题栏背景色
     */
    protected fun setToolbarBgColor(color: Int) {
        val v = provideTitleBar()
        if (v is Toolbar) {
            v.setBackgroundColor(color)
        }
    }

    /**
     * 改变标题栏内所有控件的前景色
     */
    protected fun changeToolbarChildViewColor(color: Int) {
        val v = provideTitleBar()
        if (v is Toolbar) {
            v.setTitleTextColor(color)
            for (i in 0 until v.childCount) {
                when (val child = v.getChildAt(i)) {
                    is ImageView -> child.setColorFilter(color)
                    is TextView -> child.setTextColor(color)
                    is ActionMenuView -> {
                        for (j in 0 until child.childCount) {
                            when (val c = child.getChildAt(j)) {
                                is ImageView -> c.setColorFilter(color)
                                is TextView -> c.setTextColor(color)
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * 改变标题栏菜单的颜色
     */
    protected fun changeActionViewColor(color: Int) {
        val v = provideTitleBar()
        if (v is Toolbar) {
            for (i in 0 until v.childCount) {
                when (val child = v.getChildAt(i)) {
                    is ActionMenuView -> {
                        for (j in 0 until child.childCount) {
                            when (val c = child.getChildAt(j)) {
                                is ImageView -> c.setColorFilter(color)
                                is TextView -> c.setTextColor(color)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加虚假状态栏View
     */
    protected fun useFakeStatusBar(@ColorRes statusBarColorRes: Int, isDarkFont: Boolean = true) {
        if (provideStatusBar() != null || provideTitleBar() != null) return
        val fakeStatusBar = View(this)
        fakeStatusBar.id = R.id.fakeStatusBar
        fakeStatusBar.setBackgroundResource(statusBarColorRes)
        binding.root.addFakeStatusBar(fakeStatusBar)
        ImmersionBar.with(this).apply {
            statusBarView(fakeStatusBar)
            statusBarDarkFont(isDarkFont)
        }.init()
    }

    /**
     * 设置虚假状态栏View
     */
    open fun provideStatusBar(): View? = null


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**
         * 设置返回图标点击事件
         */
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}