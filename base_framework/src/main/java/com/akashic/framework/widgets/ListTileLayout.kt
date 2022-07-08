package com.akashic.framework.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setMargins
import androidx.core.view.updateLayoutParams
import com.akashic.framework.R
import com.akashic.framework.databinding.ListTileLayoutBinding
import com.akashic.framework.ext.gone
import com.akashic.framework.ext.shape
import com.akashic.framework.ext.visible

class ListTileLayout : LinearLayout {


    private var realViewBinding: ListTileLayoutBinding

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr, 0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {

        orientation = VERTICAL

        realViewBinding = ListTileLayoutBinding.inflate(LayoutInflater.from(context))

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ListTileLayout,
            defStyleAttr,
            0
        )

        val iconHeight = typedArray.getDimension(R.styleable.ListTileLayout_ltl_icon_height, -1f)
        if (iconHeight > 0) {
            realViewBinding.icon.updateLayoutParams {
                height = iconHeight.toInt()
            }
        }

        val iconWidth = typedArray.getDimension(R.styleable.ListTileLayout_ltl_icon_width, -1f)
        if (iconWidth > 0) {
            realViewBinding.icon.updateLayoutParams {
                width = iconWidth.toInt()
            }
        }

        val iconRes = typedArray.getResourceId(R.styleable.ListTileLayout_ltl_icon, -1)
        if (iconRes > 0) {
            realViewBinding.icon.visible()
            realViewBinding.icon.setImageResource(iconRes)
        } else {
            realViewBinding.icon.gone()
        }


        val titleStr = typedArray.getString(R.styleable.ListTileLayout_ltl_title)
        realViewBinding.title.text = titleStr

        if (!titleStr.isNullOrEmpty()) {
            val titleSize = typedArray.getDimension(R.styleable.ListTileLayout_ltl_title_size, 15f)
            val titleColor =
                typedArray.getColor(R.styleable.ListTileLayout_ltl_title_color, Color.WHITE)

            realViewBinding.title.setTextColor(titleColor)
            realViewBinding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        }


        val rightIconHeight =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_right_icon_height, -1f)
        if (rightIconHeight > 0) {
            realViewBinding.rightIcon.updateLayoutParams {
                height = rightIconHeight.toInt()
            }
        }

        val rightIconWidth =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_right_icon_width, -1f)
        if (rightIconWidth > 0) {
            realViewBinding.rightIcon.updateLayoutParams {
                width = rightIconWidth.toInt()
            }
        }

        val rightIconRes = typedArray.getResourceId(R.styleable.ListTileLayout_ltl_right_icon, -1)
        if (rightIconRes > 0) {
            realViewBinding.rightIcon.visible()
            realViewBinding.rightIcon.setImageResource(rightIconRes)
        } else {
            realViewBinding.rightIcon.gone()
        }


        val corner = typedArray.getDimension(R.styleable.ListTileLayout_ltl_corners, 0f)
        val bgColor = typedArray.getColor(R.styleable.ListTileLayout_ltl_bg_color, Color.WHITE)
        val topLeftCorner =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_topLeft_corner, 0f)
        val topRightCorner =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_topRight_corner, 0f)
        val bottomLeftCorner =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_bottomLeft_corner, 0f)
        val bottomRightCorner =
            typedArray.getDimension(R.styleable.ListTileLayout_ltl_bottomRight_corner, 0f)

        background = shape {
            solid(bgColor)
            if (topLeftCorner > 0 || topRightCorner > 0 || bottomLeftCorner > 0 || bottomRightCorner > 0) {
                corners {
                    topLeft = topLeftCorner
                    topRight = topRightCorner
                    bottomLeft = bottomLeftCorner
                    bottomRight = bottomRightCorner
                }
            } else {
                corners(corner)
            }
        }

        val realPadding =
            typedArray.getDimensionPixelSize(R.styleable.ListTileLayout_ltl_padding, 0)

        //分割线
        val dividerHeight =
            typedArray.getDimensionPixelSize(R.styleable.ListTileLayout_ltl_divider_height, 0)
        val dividerColor =
            typedArray.getColor(R.styleable.ListTileLayout_ltl_divider_color, Color.TRANSPARENT)
        val dividerMarginStart =
            typedArray.getDimensionPixelSize(R.styleable.ListTileLayout_ltl_divider_marginStart, 0)
        val dividerMarginEnd =
            typedArray.getDimensionPixelSize(R.styleable.ListTileLayout_ltl_divider_marginEnd, 0)

        typedArray.recycle()

        addView(realViewBinding.root, LayoutParams(-1, -2).apply {
            this.setMargins(realPadding)
        })

        if (dividerHeight > 0 && dividerColor != Color.TRANSPARENT) {
            val dividerView = View(context).apply {
                this.setBackgroundColor(dividerColor)
            }
            addView(dividerView, LayoutParams(-1, dividerHeight).apply {
                marginStart = dividerMarginStart
                marginEnd = dividerMarginEnd
            })
        }
    }


    fun setRightView(view: View) {
        realViewBinding.rightViewContainer.removeAllViews()
        realViewBinding.rightViewContainer.addView(view)
    }
}