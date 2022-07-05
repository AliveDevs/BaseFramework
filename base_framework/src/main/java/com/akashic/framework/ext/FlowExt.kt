package com.akashic.framework.ext

import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.akashic.framework.widgets.dialog.LoadingDialog
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


/**
 * 转换到IO线程并绑定scope
 * @receiver Flow<T>
 * @param scope CoroutineScope 要绑定scope
 * @return SharedFlow<T>
 */
fun <T> Flow<T>.bind(scope: CoroutineScope): SharedFlow<T> {
    return flowOn(Dispatchers.IO).shareIn(
        scope,
        SharingStarted.Eagerly
    )
}

/**
 * 将流跟当前生命周期绑定
 */
fun <T> Flow<T>.collectIn(
    lifecycleOwner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: (T) -> Unit
): Job = lifecycleOwner.lifecycleScope.launch {
    flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
}

/**
 * 跟加载对话框绑定，用于阻塞用户操作
 * @receiver Flow<T>
 */
fun <T> Flow<T>.bindLoadingDialog(dialog: Dialog? = null): Flow<T> {
    var loadingDialog: Dialog? = null
    MainScope().launch {
        loadingDialog = dialog ?: LoadingDialog(ActivityUtils.getTopActivity())
    }
    return onStart {
        MainScope().launch {
            loadingDialog?.show()
        }

    }.onCompletion {
        MainScope().launch {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }
}