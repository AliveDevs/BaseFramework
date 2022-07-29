package com.akashic.framework.widgets.imggrid

import androidx.core.view.isVisible
import com.akashic.framework.databinding.ImgGridViewItemBinding
import com.akashic.framework.ext.*
import com.akashic.framework.ui.BaseItemDelegate

class ImgGridVH(
    private val videoIcon: Int,
    private val removeIcon: Int,
    private val radius:Int,
    private val onAddClickCallback: (() -> Unit),
    private val clickCallback: ((position: Int) -> Unit),
    private val removeCallback: ((position: Int) -> Unit)
) : BaseItemDelegate<ImgGridItemData, ImgGridViewItemBinding>() {

    override fun onBindViewHolder(
        holder: ImgGridViewItemBinding,
        item: ImgGridItemData,
        position: Int
    ) {
        if (item.isAddFlag) {
            holder.remove.gone()
            holder.videoFlag.gone()
            if (item.data is Int && (item.data as Int) > 0)
                holder.cover.bind(item.data as Int)
            holder.cover.singleClick { onAddClickCallback.invoke() }
        } else {
            val path = item.data as String
            holder.remove.visible()
            holder.remove.setImageResource(removeIcon)
            holder.videoFlag.isVisible = path.isVideoFile
            holder.videoFlag.setImageResource(videoIcon)
            holder.remove.singleClick {
                removeCallback.invoke(position)
            }
            holder.cover.bind(path, roundingRadius = radius)
            holder.cover.singleClick { clickCallback.invoke(position) }
            holder.remove.singleClick { removeCallback.invoke(position) }
        }
    }
}

data class ImgGridItemData(var data: Any, val isAddFlag: Boolean = false)