package com.akashic.framework.widgets

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager


/**
 *  Author: akatosh
 *  Date : 2019-09-12 13:18
 */
class SectionItemDecoration(
    private val bgColor: Int,
    private val textColor: Int,
    private val textSize: Int,
    private val sectionHeight:Int,
    private val listener: SectionListener
) : RecyclerView.ItemDecoration() {

    private var bgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var textPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    private var bounds: Rect = Rect()


    //private var bgColor: Int? = null

    //private var textColor: Int? = null

    // private var textSize: Int? = null

    // private var sectionHeight = 0


    init {
        bgPaint.color = bgColor

        textPaint.color = textColor

        textPaint.textSize  = textSize.toFloat()
    }


    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = 56
        val right = parent.width - parent.paddingRight
        var preSectionName: String?
        var currentSectionName: String? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            val lp = child.layoutParams as RecyclerView.LayoutParams
            preSectionName = currentSectionName
            currentSectionName = listener.getSectionName(position)
            if (position == 0 || !TextUtils.equals(preSectionName, currentSectionName)) {
                drawSection(c, left, right, child, lp, position)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (pos < 0) return
        val section = listener.getSectionName(pos)
        val child = parent.findViewHolderForLayoutPosition(pos)!!.itemView

        var flag = false
        if (pos + 1 < state.itemCount) {
            if (section != listener.getSectionName(pos + 1)) {
                if (child.height + child.top < sectionHeight) {
                    c.save()
                    flag = true
                    c.translate(0f, (child.height + child.top - sectionHeight).toFloat())
                }
            }
        }
        c.drawRect(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat(),
            (parent.right - parent.paddingRight).toFloat(),
            (parent.paddingTop + sectionHeight).toFloat(), bgPaint
        )
        textPaint.getTextBounds(section, 0, section.length, bounds)
        c.drawText(
            section,
            36f,
            (parent.paddingTop + sectionHeight - (sectionHeight / 2 - bounds.height() / 2)).toFloat(),
            textPaint
        )
        if (flag)
            c.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position > -1) {
            if (position == 0) {
                outRect.set(0, sectionHeight, 0, 0)
            } else {
                val preSectionName = listener.getSectionName(position - 1)
                val currentSectionName = listener.getSectionName(position)
                if (!TextUtils.equals(preSectionName, currentSectionName)) {
                    outRect.set(0, sectionHeight, 0, 0)
                }

            }
        }
    }


    private fun drawSection(
        c: Canvas, left: Int, right: Int, child: View,
        params: RecyclerView.LayoutParams, position: Int
    ) {
        c.drawRect(
            left.toFloat(),
            (child.top - params.topMargin - sectionHeight).toFloat(),
            right.toFloat(),
            (child.top - params.topMargin).toFloat(), bgPaint
        )
        textPaint.getTextBounds(
            listener.getSectionName(position),
            0,
            listener.getSectionName(position).length,
            bounds
        )
        c.drawText(
            listener.getSectionName(position),
            36f,
            (child.top - params.topMargin - (sectionHeight / 2 - bounds.height() / 2)).toFloat(),
            textPaint
        )
    }


    interface SectionListener {
        fun getSectionName(position: Int): String
    }


    class Builder {

        private var sectionHeight:Int = 48

        var bgColor:Int = Color.WHITE

        var textColor:Int = Color.BLACK

        var textSize:Int = 15

        var sectionListener: SectionListener? = null

        fun setHeight(height: Int): Builder {
            this.sectionHeight = height
            return this
        }

        fun setBackgroundColor(bgColor: Int): Builder {
            this.bgColor = bgColor
            return this
        }

        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        fun setTextSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun setListener(listener: SectionListener){
            this.sectionListener = listener
        }

        fun build(): SectionItemDecoration {
            return SectionItemDecoration(bgColor,textColor,textSize,sectionHeight,sectionListener!!)
        }
    }
}