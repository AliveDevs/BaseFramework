package com.akashic.framework.ui.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.akashic.framework.ext.collectIn
import com.akashic.framework.ui.BaseBindingFragment
import com.akashic.framework.widgets.load.LoadListLayout
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

abstract class BaseListBindingFragment<VB : ViewBinding, VM : BaseListViewModel> : BaseBindingFragment<VB>() {


    /**
     *  封装的列表加载控件
     */
    abstract val loadListLayout: LoadListLayout

    /**
     * 业务数据处理VM
     */
    abstract val model: VM

    /**
     * 列表界面基本事件流
     */
    private val listIntents by lazy {
        merge(
            callbackFlow {
                loadListLayout.setRefreshListener {
                    trySend(BaseListIntent.RefreshIntent)
                }
                awaitClose()
            },
            callbackFlow {
                loadListLayout.setLoadMoreListener {
                    trySend(BaseListIntent.LoadMoreDataIntent)
                }
                awaitClose()
            }
        )
    }

    /**
     * 是否在每次展示当前界面时加载数据（仅列表为空时）
     */
    open val loadWhenResume = true

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listIntents.onEach(model::receive).launchIn(lifecycleScope)
        model.listState.collectIn(this) { updateUIbyState(it) }
    }

    override fun onResume() {
        super.onResume()
        if (loadListLayout.items.isEmpty() && loadWhenResume) {
            loadListLayout.refreshLoadLayout.autoRefresh()
        }
    }

    /**
     * 状态分发并刷新界面
     * @param uiState BaseListUiState 界面状态描述
     */
    private fun updateUIbyState(uiState: BaseListUiState) {
        when (uiState) {
            is BaseListUiState.Success -> {
                loadSucceed(uiState.list, uiState.noMoreData, uiState.clearOldData)
            }
            is BaseListUiState.Init ->{
                initList(uiState.list)
            }
            is BaseListUiState.Loading -> {
                showLoading(uiState.tip)
            }
            is BaseListUiState.Empty -> {
                showEmpty(uiState.tip)
            }
            is BaseListUiState.Error -> {
                showError(uiState.errorMsg)
            }
        }
    }

    /**
     * 列表数据成功加载的回调
     * @param dataList MutableList<Any> 要展示的列表数据
     * @param noMoreData Boolean 是否还有更多数据
     * @param clearOldData Boolean 是否需要清除旧数据
     */
    open fun loadSucceed(
        dataList: MutableList<Any>,
        noMoreData: Boolean,
        clearOldData: Boolean = false
    ) {
        loadListLayout.loadDataSuccess(dataList, noMoreData, clearOldData)
    }

    /**
     * 首次加载显示的默认列表
     * @param list MutableList<Any>
     */
    open fun initList(list: MutableList<Any>){

    }

    /**
     * 显示加载状态布局
     * @param tip String? 提示文字
     */
    open fun showLoading(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showLoading()
            return
        }
        loadListLayout.showLoading(tip)
    }

    /**
     * 显示空白状态布局
     * @param tip String? 提示文字
     */
    open fun showEmpty(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showEmpty()
            return
        }
        loadListLayout.showEmpty(tip)
    }


    /**
     * 显示错误状态布局
     * @param tip String? 提示文字
     */
    open fun showError(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showError()
            return
        }
        loadListLayout.showError(tip)
    }

}