package com.akashic.framework.widgets.dialog

import android.content.Context
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.akashic.framework.R
import com.akashic.framework.ext.dp
import com.blankj.utilcode.util.ScreenUtils
import com.dylanc.viewbinding.base.ViewBindingUtil

abstract class BaseBindingDialog<VB : ViewBinding>(context: Context, themeResId: Int = R.style.BaseDialog):BaseDialog(context, themeResId) {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        setContentView(binding.root)
        val layoutParams = binding.root.layoutParams
        layoutParams.width = ScreenUtils.getScreenWidth() - 100.dp
        binding.root.layoutParams = layoutParams
        setCanceledOnTouchOutside(true)
    }
}