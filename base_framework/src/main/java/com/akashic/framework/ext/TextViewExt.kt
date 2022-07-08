package com.akashic.framework.ext

import android.text.InputFilter
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.widget.EditText
import android.widget.TextView
import com.akashic.framework.widgets.span.CenterAlignImageSpan
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.ResourceUtils

/**
 * 获取文本内容
 */
val TextView.txt
    get():String {
        return if (this.text == null) "" else this.text.toString()
    }


/**
 * 是否是可见的密码输入
 */
fun TextView.isVisiblePasswordType() =
    inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)

/**
 * 将内容类型切换为可见的密码输入
 */
fun TextView.change2VisiblePasswordType() {
    this.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
}


/**
 * 是否是可见的密码输入
 */
fun TextView.isVisibleNumberPasswordType() =
    inputType == (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL)

/**
 * 将内容类型切换为可见的密码输入
 */
fun TextView.change2NumberVisiblePasswordType() {
    this.inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
}

/**
 * 将内容类型切换为不可见的密码输入
 */
fun TextView.change2NumberPasswordType() {
    this.inputType =
        InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
}


/**
 * 是否是可见的密码输入
 */
fun TextView.isPasswordType() =
    inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)

/**
 * 将内容类型切换为不可见的密码输入
 */
fun TextView.change2PasswordType() {
    this.inputType =
        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
}

/**
 * 将光标移动到最后
 */
fun EditText.cursorMoveToLast() {
    this.setSelection(this.text?.length ?: 0)
}

/**
 * 增加前缀图标
 */
fun TextView.addPreIcons(preIconRes: ArrayList<Int>) {
    var str = text

    for (index in 0 until preIconRes.size) {
        str = " $str"
    }
    val spanStr = SpannableString(str)
    for (index in 0 until preIconRes.size) {
        val icon = ResourceUtils.getDrawable(preIconRes[index])
        icon.setBounds(0, 0, icon.minimumWidth, icon.minimumHeight)
        spanStr.setSpan(
            CenterAlignImageSpan(icon),
            index,
            index + 1,
            Spannable.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
    text = spanStr
}

/**
 * 限制输入中文
 * @receiver EditText
 */
fun EditText.limitChinese(){
    val newFilters=this.filters.toMutableList()
    newFilters.add(InputFilter { charSequence, _, _, _, _, _ ->
        if(charSequence.isNullOrEmpty() || RegexUtils.isZh(charSequence)) "" else charSequence
    })
    this.filters = newFilters.toTypedArray()
}
