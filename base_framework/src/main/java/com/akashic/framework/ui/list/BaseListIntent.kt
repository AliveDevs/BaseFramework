package com.akashic.framework.ui.list

sealed interface BaseListIntent{

    object RefreshIntent : BaseListIntent

    object LoadMoreDataIntent : BaseListIntent

    data class RemoveItemIntent(val position:Int):BaseListIntent
}

