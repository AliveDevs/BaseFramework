package com.akashic.framework.ui.list

import com.akashic.framework.databinding.CommonLoadListLayoutBinding
import com.akashic.framework.widgets.load.LoadListLayout

abstract class SimpleListBindingFragment<VM:BaseListViewModel>:BaseListBindingFragment<CommonLoadListLayoutBinding,VM>() {

    override val loadListLayout: LoadListLayout
        get() = binding.root

}