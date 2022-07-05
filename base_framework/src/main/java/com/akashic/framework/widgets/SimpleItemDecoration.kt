package com.akashic.framework.widgets

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SimpleItemDecoration(private val spacing: Int, private val crossSpacing: Int = 0) :
    RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //布局管理器
        val layoutManager = parent.layoutManager
        //索引
        val pos = parent.getChildAdapterPosition(view)


        when (layoutManager) {
            is LinearLayoutManager -> {
                val isHorizontal = layoutManager.orientation == RecyclerView.HORIZONTAL
                ///第一个
                if (pos == 0) {
                    if(isHorizontal){
                        outRect.left = spacing
                    }else{
                        outRect.top = spacing
                    }
                }
                if (isHorizontal) {
                    outRect.top = crossSpacing
                    outRect.bottom = crossSpacing
                }else{
                    outRect.left = crossSpacing
                    outRect.right = crossSpacing
                }

                if(isHorizontal){
                    outRect.right = crossSpacing
                }else{
                    outRect.bottom = crossSpacing
                }
            }
            is StaggeredGridLayoutManager -> {
                val params: StaggeredGridLayoutManager.LayoutParams =
                    view.layoutParams as StaggeredGridLayoutManager.LayoutParams

                if (parent.getChildAdapterPosition(view) < 2 && !params.isFullSpan) {
                    outRect.top = spacing
                }

                if(params.isFullSpan){
                    outRect.left = spacing
                    outRect.right = spacing
                    outRect.bottom = spacing
                    return
                }

                if (params.spanIndex % 2 == 0) {
                    outRect.left = spacing
                    outRect.right = spacing / 2
                } else {
                    outRect.left = spacing / 2
                    outRect.right = spacing
                }

                outRect.bottom = spacing
            }
        }
    }
}