package com.akashic.framework.ui.list

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.akashic.framework.ext.collectIn
import com.akashic.framework.ui.BaseBindingActivity
import com.akashic.framework.widgets.load.LoadListLayout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


abstract class BaseListBindingActivity<VB : ViewBinding, VM : BaseListViewModel> : BaseBindingActivity<VB>(){

    //封装的列表加载控件
    abstract val loadListLayout: LoadListLayout

    //业务数据处理VM
    abstract val model: VM


    // 组织界面事件
    private val listIntents by lazy {
        merge(
            refreshList(),
            loadMoreData()
        )
    }

    open val loadWhenResume = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listIntents.onEach(model::receive).launchIn(lifecycleScope)
        model.listState.collectIn(this) { updateUIbyState(it) }
    }

    override fun onResume() {
        super.onResume()
        if (loadListLayout.items.isEmpty() && loadWhenResume) {
            loadListLayout.refreshLoadLayout.autoRefresh()
        }
    }

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

    open fun showLoading(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showLoading()
            return
        }
        loadListLayout.showLoading(tip)
    }

    open fun showEmpty(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showEmpty()
            return
        }
        loadListLayout.showEmpty(tip)
    }


    open fun showError(tip: String? = null) {
        if (tip.isNullOrEmpty()) {
            loadListLayout.showError()
            return
        }
        loadListLayout.showError(tip)
    }

    private fun refreshList(): Flow<BaseListIntent> = callbackFlow {
        loadListLayout.setRefreshListener {
            trySend(BaseListIntent.RefreshIntent)
        }
        awaitClose()
    }


    private fun loadMoreData(): Flow<BaseListIntent> = callbackFlow {
        loadListLayout.setLoadMoreListener {
            trySend(BaseListIntent.LoadMoreDataIntent)
        }
        awaitClose()
    }

}