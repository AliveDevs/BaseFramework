package com.akashic.framework.ui.list

import androidx.lifecycle.viewModelScope
import com.akashic.framework.BaseConstants
import com.akashic.framework.ext.bind
import com.akashic.framework.ext.debug
import com.akashic.framework.ext.msg
import com.akashic.framework.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


abstract class BaseListViewModel : BaseViewModel() {

    open var currentIndex = BaseConstants.DEFAULT_INDEX

    private val _listIntent = MutableSharedFlow<BaseListIntent>()


    val listState = _listIntent.toStateFlow().bind(viewModelScope)

    /**
     * 接收意图
     * @param intent BaseListIntent
     */
    fun receive(intent: BaseListIntent) {
        viewModelScope.launch { _listIntent.emit(intent) }
    }

    @FlowPreview
    private fun Flow<BaseListIntent>.toStateFlow(): Flow<BaseListUiState> = merge(
        filterIsInstance<BaseListIntent.RefreshIntent>().flatMapConcat { refreshList() },
        filterIsInstance<BaseListIntent.LoadMoreDataIntent>().flatMapConcat { loadList(false) },
        filterIsInstance<BaseListIntent.RemoveItemIntent>().flatMapConcat { removeItem(it.position) }
    ).catch { emit(BaseListUiState.Error(it.msg)) }


    /**
     * 刷新列表
     * @return Flow<BaseListUiState>
     */
    private suspend fun refreshList(): Flow<BaseListUiState> {
        currentIndex = BaseConstants.DEFAULT_INDEX
        return loadList(true)
    }

    /**
     *  加载列表数据
     * @param isRefresh Boolean
     * @return Flow<BaseListUiState> 以流的形式返回页面状态
     */
    protected abstract suspend fun loadList(isRefresh: Boolean = false): Flow<BaseListUiState>

    /**
     *  根据返回的列表数据转换为页面状态
     * @receiver List<Any>
     * @param isRefresh Boolean 是否是刷新
     * @param pageSize Int 一次请求的数据量
     * @return Flow<BaseListUiState> 以流的形式返回页面状态
     */
    protected fun List<Any>.transformToUiState(
        isRefresh: Boolean = false,
        pageSize: Int = 10
    ): Flow<BaseListUiState> {
        return flowOf(this).map {
            val noMoreData = it.size < pageSize
            currentIndex++
            if (it.isEmpty() && isRefresh) BaseListUiState.Empty() else BaseListUiState.Success(
                it.toMutableList(),
                noMoreData,
                isRefresh
            )
        }.onStart {
            if (isRefresh) {
                emit(onLoadListStart())
            }
        }
    }

    open fun onLoadListStart(): BaseListUiState {
        return BaseListUiState.Loading()
    }

    open fun removeItem(position:Int):Flow<BaseListUiState>{
        return flowOf(BaseListUiState.RemoveSucceeded(position))
    }
}