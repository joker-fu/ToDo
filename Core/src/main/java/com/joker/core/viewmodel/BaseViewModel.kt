package com.joker.core.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joker.core.recycler.bean.ViewModelAction

/**
 * BaseViewModel
 *
 * @author joker
 * @date 2019/1/23.
 */
abstract class BaseViewModel<T> : ViewModel() {

    abstract val repository: T

    val viewModelAction by lazy {
        MutableLiveData<ViewModelAction>()
    }

    fun applyStatus(state: ViewModelAction) {
        viewModelAction.postValue(state)
    }
}