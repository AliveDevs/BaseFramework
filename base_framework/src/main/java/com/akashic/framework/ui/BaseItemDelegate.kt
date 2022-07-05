package com.akashic.framework.ui

import android.content.Context
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewbinding.ViewBinding
import com.drakeet.multitype.ItemViewDelegate
import com.dylanc.viewbinding.base.ViewBindingUtil.inflateWithGeneric


/**
 *  author: akashic
 *
 *  date: 2021-03-14 2:11 PM
 *
 *  desc: 列表项基类
 */
abstract class BaseItemDelegate<T:Any, VB : ViewBinding> :
    ItemViewDelegate<T, BaseItemDelegate.BindingViewHolder<VB>>() {

    /**
     * 是否占满格位，用于瀑布流布局
     */
    open val fullSpan = false

    override fun onCreateViewHolder(context: Context, parent: ViewGroup) =
        BindingViewHolder(inflateWithGeneric<VB>(this, parent))

    override fun onBindViewHolder(holder: BindingViewHolder<VB>, item: T) {
        onBindViewHolder(holder.binding, item, holder.bindingAdapterPosition)
    }

    abstract fun onBindViewHolder(holder: VB, item: T, position: Int)

    override fun onViewAttachedToWindow(holder: BindingViewHolder<VB>) {
        super.onViewAttachedToWindow(holder)
        if (fullSpan && holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            holder.itemView.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                isFullSpan = true
            }
        }
    }

    class BindingViewHolder<VB : ViewBinding>(val binding: VB) :
        RecyclerView.ViewHolder(binding.root)
}