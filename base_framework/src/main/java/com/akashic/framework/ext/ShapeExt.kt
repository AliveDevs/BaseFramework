package com.akashic.framework.ext

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import kotlin.math.absoluteValue

/**
 * https://github.com/xiazunyang/drawable.dsl
 */

class ColorSelectorBuilder {

    var defColor: Int? = null
    private var defColorBuilder: (() -> Int)? = null

    private val builders = mutableListOf<StateBuilder>()

    fun defState(color: Int): ColorSelectorBuilder {
        defColor = color
        return this
    }

    fun defState(builder: () -> Int): ColorSelectorBuilder {
        defColorBuilder = builder
        return this
    }

    fun addState(stateElement: StateElement, color: Int): ColorSelectorBuilder {
        val stateBuilder = StateBuilder(stateElement)
        stateBuilder.color = color
        builders.add(stateBuilder)
        return this
    }

    fun addState(stateElement: StateElement, builder: () -> Int): ColorSelectorBuilder {
        val stateBuilder = StateBuilder(stateElement)
        stateBuilder.color(builder)
        builders.add(stateBuilder)
        return this
    }

    fun build(): ColorStateList {
        val stateList = mutableListOf<IntArray>()
        val colorList = mutableListOf<Int>()
        for (b in builders) {
            val color = b.color ?: b.colorBuilder?.invoke() ?: throw NullPointerException()
            stateList.add(b.stateElement.states)
            colorList.add(color)
        }
        val defColor = defColor ?: defColorBuilder?.invoke()
        if (defColor != null) {
            stateList.add(intArrayOf())
            colorList.add(defColor)
        }
        return ColorStateList(stateList.toTypedArray(), colorList.toIntArray())
    }

    class StateBuilder(val stateElement: StateElement) {
        var color: Int? = null
        internal var colorBuilder: (() -> Int)? = null

        fun color(builder: () -> Int) {
            colorBuilder = builder
        }

    }

}

fun colorSelector(builder: ColorSelectorBuilder.() -> Unit): ColorStateList {
    val colorSelectorBuilder = ColorSelectorBuilder()
    colorSelectorBuilder.builder()
    return colorSelectorBuilder.build()
}

fun shape(shape: Shape = Shape.RECTANGLE, builder: ShapeBuilder.() -> Unit): GradientDrawable {
    val shapeBuilder = ShapeBuilder(shape)
    shapeBuilder.builder()
    return shapeBuilder.build()
}

fun selector(builder: SelectorBuilder.() -> Unit): StateListDrawable {
    val stateListBuilder = SelectorBuilder()
    stateListBuilder.builder()
    return stateListBuilder.build()
}

fun ripple(builder: RippleBuilder.() -> Unit): RippleDrawable {
    val rippleBuilder = RippleBuilder()
    rippleBuilder.builder()
    return rippleBuilder.build()
}

class RippleBuilder {

    var color: ColorStateList? = null

    var content: Drawable? = null
    var contentBuilder: (() -> Drawable)? = null

    var mask: Drawable? = null
    var maskBuilder: (() -> Drawable)? = null

    fun color(color: Int): RippleBuilder {
        this.color = ColorStateList.valueOf(color)
        return this
    }

    fun content(builder: () -> Drawable): RippleBuilder {
        contentBuilder = builder
        return this
    }

    fun mask(builder: () -> Drawable): RippleBuilder {
        maskBuilder = builder
        return this
    }

    fun build(): RippleDrawable = RippleDrawable(
        color ?: throw NullPointerException(),
        content ?: contentBuilder?.invoke(),
        mask ?: maskBuilder?.invoke()
    )

}

class SelectorBuilder {

    private var default: Drawable? = null
    private var defaultBuilder: (() -> Drawable)? = null
    private var builders = mutableListOf<StateBuilder>()

    fun defState(drawable: Drawable): SelectorBuilder {
        this.default = drawable
        return this
    }

    fun defState(builder: () -> Drawable): SelectorBuilder {
        defaultBuilder = builder
        return this
    }

    fun addState(state: StateElement, drawable: Drawable): SelectorBuilder {
        val stateBuilder = StateBuilder(state)
        stateBuilder.drawable(drawable)
        builders.add(stateBuilder)
        return this
    }

    fun addState(state: StateElement, builder: () -> Drawable): SelectorBuilder {
        val stateBuilder = StateBuilder(state)
        stateBuilder.drawable(builder)
        builders.add(stateBuilder)
        return this
    }

    fun build(): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        for (builder in builders) {
            stateListDrawable.addState(
                builder.stateElement.states,
                builder.drawable ?: builder.drawableBuilder?.invoke()
            )
        }
        val defaultDrawable = default ?: defaultBuilder?.invoke()
        if (defaultDrawable != null) {
            stateListDrawable.addState(intArrayOf(), defaultDrawable)
        }
        return stateListDrawable
    }

    class StateBuilder(val stateElement: StateElement) {
        var drawable: Drawable? = null
        var drawableBuilder: (() -> Drawable)? = null

        fun drawable(builder: () -> Drawable): StateBuilder {
            this.drawableBuilder = builder
            return this
        }

        fun drawable(drawable: Drawable): StateBuilder {
            this.drawable = drawable
            return this
        }

    }

}

enum class Shape {
    RECTANGLE,
    OVAL,
    LINE,
    RING;
}

enum class GradientType {
    LINEAR,
    RADIAL,
    SWEEP;
}

class ShapeBuilder(private val shape: Shape) {

    private var sizeBuilder = SizeBuilder()
    private var strokeBuilder = StrokeBuilder()
    private var cornersBuilder = CornersBuilder()
    private var paddingBuilder = PaddingBuilder()
    private var gradientBuilder = GradientBuilder()

    var solid: ColorStateList? = null
    var orientation = GradientDrawable.Orientation.TOP_BOTTOM

    fun solid(color: Int): ShapeBuilder {
        this.solid = ColorStateList.valueOf(color)
        return this
    }

    fun corners(builder: CornersBuilder.() -> Unit): ShapeBuilder {
        cornersBuilder.builder()
        return this
    }

    fun corners(corners: Float): ShapeBuilder {
        cornersBuilder.topLeft = corners
        cornersBuilder.topRight = corners
        cornersBuilder.bottomLeft = corners
        cornersBuilder.bottomRight = corners
        return this
    }

    fun padding(builder: PaddingBuilder.() -> Unit): ShapeBuilder {
        paddingBuilder.builder()
        return this
    }

    fun padding(padding: Int): ShapeBuilder {
        paddingBuilder.left = padding
        paddingBuilder.top = padding
        paddingBuilder.right = padding
        paddingBuilder.bottom = padding
        return this
    }

    fun stroke(builder: StrokeBuilder.() -> Unit): ShapeBuilder {
        strokeBuilder.builder()
        return this
    }

    fun size(width: Int, height: Int): ShapeBuilder {
        sizeBuilder.width = width
        sizeBuilder.height = height
        return this
    }

    fun size(builder: SizeBuilder.() -> Unit): ShapeBuilder {
        sizeBuilder.builder()
        return this
    }

    fun gradient(builder: GradientBuilder.() -> Unit): ShapeBuilder {
        gradientBuilder.builder()
        return this
    }

    fun build(): GradientDrawable {
        val gradientDrawable = GradientDrawable()

        gradientDrawable.orientation = orientation
        val colors = gradientBuilder.colors
        if (colors.size < 2) {
            gradientDrawable.color = solid
        } else {
            gradientDrawable.colors = colors
        }
        gradientDrawable.shape = shape.ordinal
        gradientDrawable.useLevel = gradientBuilder.useLevel
        gradientDrawable.gradientType = gradientBuilder.type.ordinal
        gradientDrawable.gradientRadius = gradientBuilder.radius
        gradientDrawable.setGradientCenter(gradientBuilder.centerX, gradientBuilder.centerY)

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            gradientDrawable.setPadding(
//                paddingBuilder.left,
//                paddingBuilder.top,
//                paddingBuilder.right,
//                paddingBuilder.bottom
//            )
//        }

        gradientDrawable.setStroke(
            strokeBuilder.width,
            strokeBuilder.color,
            strokeBuilder.dashWidth,
            strokeBuilder.dashGap
        )

        gradientDrawable.cornerRadii = floatArrayOf(
            cornersBuilder.topLeft,
            cornersBuilder.topLeft,
            cornersBuilder.topRight,
            cornersBuilder.topRight,
            cornersBuilder.bottomRight,
            cornersBuilder.bottomRight,
            cornersBuilder.bottomLeft,
            cornersBuilder.bottomLeft
        )

        return gradientDrawable
    }

    class SizeBuilder {
        var width: Int = 0
        var height: Int = 0
    }

    class CornersBuilder {
        var topLeft: Float = 0f
        var topRight: Float = 0f
        var bottomLeft: Float = 0f
        var bottomRight: Float = 0f
    }

    class PaddingBuilder {
        var left: Int = 0
        var top: Int = 0
        var right: Int = 0
        var bottom: Int = 0
    }

    class StrokeBuilder {
        var width: Int = 0
        var color: Int = Color.TRANSPARENT
        private var dash: DashBuilder = DashBuilder()

        var dashWidth: Float by dash::width

        var dashGap: Float by dash::gap

        fun dash(builder: DashBuilder.() -> Unit) {
            dash.builder()
        }

        class DashBuilder {
            var width: Float = 0f
            var gap: Float = 0f
        }

    }

    class GradientBuilder {
        private var color = ColorBuilder()
        var type = GradientType.LINEAR
        var radius: Float = 0f
        var centerX: Float = 0f
        var centerY: Float = 0f
        var useLevel: Boolean = false
        var endColor: Int? by color::end
        var startColor: Int? by color::start
        var centerColor: Int? by color::center

        val colors: IntArray
            get() = listOfNotNull(startColor, centerColor, endColor).toIntArray()

        fun color(color: Int) = color(color, color)

        fun color(start: Int, end: Int, center: Int? = null) {
            color.end = end
            color.start = start
            color.center = center
        }

        fun color(builder: ColorBuilder.() -> Unit) {
            color.builder()
        }

        class ColorBuilder {

            var start: Int? = null
            var center: Int? = null
            var end: Int? = null

        }
    }

}

/** 表示一种控件的状态 */
interface StateElement {

    val states: IntArray

    operator fun plus(other: SingleStateElement): StateElement

}

/** 表示一种不可变的控件状态 */
interface SingleStateElement : StateElement {

    val state: Int

    override val states: IntArray
        get() = intArrayOf(state)

    operator fun not(): SingleStateElement = VariationalState(-state)

    override operator fun plus(other: SingleStateElement): StateElement {
        return if (other.state.absoluteValue == state.absoluteValue) {
            other
        } else {
            CombinedState(other.state, this.state)
        }
    }

}

/** 表示多种控件的状态 */
interface CombinedStateElement : StateElement {

    override val states: IntArray

    override fun plus(other: SingleStateElement): StateElement {
        val otherAbsValue = other.state.absoluteValue
        val index = states.indexOfFirst {
            it.absoluteValue == otherAbsValue
        }
        return if (index == -1) {
            CombinedState(other.state, *states)
        } else {
            val newStates = states.copyOf()
            newStates[index] = other.state
            CombinedState(*newStates)
        }
    }

}

/** 表示一种已改变的状态 */
private class VariationalState(override val state: Int) : SingleStateElement

/** 表示多种状态 */
private class CombinedState(override vararg val states: Int) : CombinedStateElement

/** 预设的可用状态 */
enum class State(override val state: Int) : SingleStateElement {

    FOCUSED(android.R.attr.state_focused),
    WINDOW_FOCUSED(android.R.attr.state_window_focused),
    ENABLED(android.R.attr.state_enabled),
    CHECKABLE(android.R.attr.state_checkable),
    CHECKED(android.R.attr.state_checked),
    SELECTED(android.R.attr.state_selected),
    PRESSED(android.R.attr.state_pressed),
    ACTIVATED(android.R.attr.state_activated),
    ACTIVE(android.R.attr.state_active),
    Single(android.R.attr.state_single),
    FIRST(android.R.attr.state_first),
    MIDDLE(android.R.attr.state_middle),
    LAST(android.R.attr.state_last),
    ACCELERATED(android.R.attr.state_accelerated),
    HOVERED(android.R.attr.state_hovered),
    DRAG_CAN_ACCEPT(android.R.attr.state_drag_can_accept),
    DRAG_HOVERED(android.R.attr.state_drag_hovered);

}