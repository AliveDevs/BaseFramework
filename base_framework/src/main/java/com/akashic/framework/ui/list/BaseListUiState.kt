package com.akashic.framework.ui.list

sealed interface BaseListUiState {

    data class Init(val list: MutableList<Any>) : BaseListUiState

    data class Loading(val tip: String? = null) : BaseListUiState

    data class Error(val errorMsg: String) : BaseListUiState

    data class Empty(val tip: String? = null) : BaseListUiState

    data class Success(
        val list: MutableList<Any>,
        val noMoreData: Boolean,
        val clearOldData: Boolean = false
    ) : BaseListUiState


}