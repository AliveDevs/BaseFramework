package com.akashic.framework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.akashic.framework.R
import com.akashic.framework.ext.addFakeStatusBar
import com.alibaba.android.arouter.launcher.ARouter
import com.dylanc.viewbinding.base.ViewBindingUtil.inflateWithGeneric
import com.gyf.immersionbar.ImmersionBar

abstract class BaseBindingFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding: VB get() = _binding!!

    ///切换到当前fragment时 状态栏字体颜色
    open val currentStatusBarFontDark: Boolean? = null

    ///是否使用ARouter注入
    open val injectByARouter = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateWithGeneric(this, layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (injectByARouter)
            ARouter.getInstance().inject(this)
        initView(savedInstanceState)
    }

    /**
     * 初始化视图
     * @param savedInstanceState Bundle?
     */
    abstract fun initView(savedInstanceState: Bundle?)

    override fun onResume() {
        super.onResume()
        if (currentStatusBarFontDark != null) {
            useStatusBarDarkFont(currentStatusBarFontDark == true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 更改状态栏前景色
     * @param useDark Boolean 是否为暗色
     */
    protected fun useStatusBarDarkFont(useDark: Boolean) {
        ImmersionBar.with(this).apply {
            statusBarDarkFont(useDark)
        }.init()
    }

    /**
     * 设置标题栏
     * @param titleBarView View 作为标题栏的View
     * @param isDarkFont Boolean 设置状态栏前景色是否为暗色
     */
    protected fun initTitleBar(titleBarView: View, isDarkFont: Boolean = true) {
        ImmersionBar.with(this).apply {
            titleBar(titleBarView)
            statusBarDarkFont(isDarkFont)
            init()
        }
    }


    /**
     * 设置虚拟状态栏占位
     * @param statusBarColorRes Int 颜色资源ID
     * @param isDarkFont Boolean 设置状态栏前景色是否为暗色
     */
    protected fun useFakeStatusBar(@ColorRes statusBarColorRes: Int, isDarkFont: Boolean = true) {
        val fakeStatusBar = View(context)
        fakeStatusBar.id = R.id.fakeStatusBar
        fakeStatusBar.setBackgroundResource(statusBarColorRes)
        binding.root.addFakeStatusBar(fakeStatusBar)
        ImmersionBar.with(this).apply {
            statusBarView(fakeStatusBar)
            statusBarDarkFont(isDarkFont)
            init()
        }
    }


}