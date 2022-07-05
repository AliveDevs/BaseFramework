package com.akashic.framework.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akashic.framework.Work
import com.akashic.framework.ext.debug
import com.akashic.framework.ext.msg
import com.akashic.framework.ext.toast
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected fun startWork(work: Work.() -> Unit) {
        viewModelScope.launch {
            val w = Work()
            try {
                w.work()
                if (!w.funcOnStart()) onWorkStart()
                w.funcWork.invoke(this)
            } catch (e: Exception) {
                if (e is CancellationException) {
                    return@launch
                }
                if (!w.funcOnError(e))
                    onWorkError(e)
            } finally {
                if (!w.funcOnFinally()) onWorkFinally()
            }

        }
    }


    protected fun onWorkStart() {

    }

    protected fun onWorkError(error: Throwable?) {
        toast(error?.msg)
        debug(error)
    }

    protected fun onWorkFinally() {

    }
}