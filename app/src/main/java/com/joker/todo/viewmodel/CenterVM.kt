package com.joker.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.joker.core.ext.wrap
import com.joker.core.http.get
import com.joker.core.utils.JLog
import com.joker.core.viewmodel.BaseViewModel
import com.joker.todo.model.repository.CenterRepository
import kotlinx.coroutines.launch

/**
 * CenterVM
 *
 * @author  joker
 * @date    2019/7/10
 */
class CenterVM : BaseViewModel<CenterRepository>() {

    override val repository: CenterRepository
        get() = CenterRepository()

    val isShowContent by lazy {
        MutableLiveData<Boolean>()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout().get()?.let {
                    JLog.d(it.toString())
                }
            } catch (e: Exception) {
                JLog.d(e.wrap().toString())
            }
        }
    }

}
