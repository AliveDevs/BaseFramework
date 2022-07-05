package com.akashic.framework.widgets.dialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import com.akashic.framework.databinding.PrivacyAgreementDialogBinding
import com.akashic.framework.ext.*

class PrivacyAgreementDialog(context: Context) :
    BaseBindingDialog<PrivacyAgreementDialogBinding>(context) {

    /**
     * 要显示的app名称
     */
    private var appName = ""

    /**
     * 自定义文本内容
     */
    private var customContent: CharSequence? = null

    /**
     * 高亮背景色
     */
    private var activeBgColor = Color.BLUE

    /**
     * 用户协议链接
     */
    private var userAgreementUrl = ""

    /**
     * 隐私政策链接
     */
    private var privacyPolicyUrl = ""

    /**
     * 同意按钮点击
     */
    private var onAgreeButtonClick: (() -> Unit)? = null

    /**
     * 不同意按钮点击
     */
    private var onDisagreeButtonClick: (() -> Unit)? = null

    /**
     * 链接点击
     */
    private var onLinkClick: ((url: String) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        binding.root.background = shape {
            corners(10f.dp)
            solid(Color.WHITE)
        }

        if (customContent != null) {
            binding.content.text = customContent
        } else {
            val sb = SpannableStringBuilder().apply {
                append("欢迎使用${appName}！\n\n在使用我们的产品服务前，请您先阅读并了解")
                append("《服务协议》".withLink(userAgreementUrl) {
                    onLinkClick?.invoke(it)
                })
                append("和")
                append("《隐私政策》".withLink(privacyPolicyUrl) {
                    onLinkClick?.invoke(it)
                })
                append("。\n\n我们将严格按照上述协议为您提供服务，保护您的信息安全，点击“同意”即表示您已阅读并同意全部条款，可以继续使用我们的产品和服务。")
            }
            binding.content.text = sb
        }
        binding.content.setLinkTextColor(activeBgColor)
        binding.content.highlightColor = activeBgColor

        binding.agreeBtn.background =
            shape {
                corners(32f.dp)
                solid(activeBgColor)
            }

        binding.agreeBtn.singleClick {
            dismiss()
            onAgreeButtonClick?.invoke()
        }

        binding.disagreeBtn.singleClick {
            dismiss()
            onDisagreeButtonClick?.invoke()
        }
    }

    /**
     * 设置要显示的APP名称
     * @param name String APP名称
     */
    fun setAppName(name: String) {
        appName = name
    }

    /**
     * 设置自定义文字内容 [setUserAgreementUrl] [setPrivacyPolicyUrl] [setLinkClick] 将失效
     * @param content CharSequence 要展示的自定义内容
     */
    fun setCustomContent(content: CharSequence) {
        customContent = content
    }

    /**
     * 设置用户协议链接
     * @param url String 链接
     */
    fun setUserAgreementUrl(url: String) {
        userAgreementUrl = url
    }

    /**
     * 设置隐私政策链接
     * @param url String 链接
     */
    fun setPrivacyPolicyUrl(url: String) {
        privacyPolicyUrl = url
    }

    /**
     * 设置统一按钮背景色
     * @param color Int 背景色值
     */
    fun setActiveColor(color: Int) {
        activeBgColor = color
    }

    /**
     * 设置同意按钮点击事件
     * @param onClick Function0<Unit>? 点击回调事件
     */
    fun setOnAgreeButtonClick(onClick: (() -> Unit)? = null) {
        onAgreeButtonClick = onClick
    }

    /**
     * 设置不同意按钮点击事件
     * @param onClick Function0<Unit>? 点击回调事件
     */
    fun setOnDisagreeButtonClick(onClick: (() -> Unit)? = null) {
        onDisagreeButtonClick = onClick
    }

    /**
     * 设置链接点击事件
     * @param onClick Function1<[@kotlin.ParameterName] String, Unit>? 点击回调事件
     */
    fun setLinkClick(onClick: ((url: String) -> Unit)? = null) {
        onLinkClick = onClick
    }

}