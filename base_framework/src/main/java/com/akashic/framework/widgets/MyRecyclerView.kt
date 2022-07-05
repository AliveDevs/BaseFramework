package com.akashic.framework.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView : RecyclerView {

    var topFadingEdgeEnable = true

    var bottomFadingEdgeEnable = false


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )


    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getTopFadingEdgeStrength(): Float {
        return if (topFadingEdgeEnable) super.getTopFadingEdgeStrength() else 0f
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return if (bottomFadingEdgeEnable) super.getBottomFadingEdgeStrength() else 0f
    }
}