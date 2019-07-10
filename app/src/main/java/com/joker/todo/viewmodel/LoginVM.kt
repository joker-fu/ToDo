package com.joker.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.joker.core.http.get
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.utils.SPUtils
import com.joker.core.viewmodel.BaseViewModel
import com.joker.todo.common.SP_USER
import com.joker.todo.model.repository.ToDoRepository
import kotlinx.coroutines.launch

/**
 * LoginVM
 *
 * @author  joker
 * @date    2019/7/10
 */
class LoginVM : BaseViewModel<ToDoRepository>() {

    override val repository: ToDoRepository
        get() = ToDoRepository()

    val loginResult = MutableLiveData<Boolean>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                applyStatus(ViewModelAction.loading())
                repository.login(username, password).get()?.let {
                    applyStatus(ViewModelAction.success())
                    SPUtils.put(SP_USER, it)
                    loginResult.postValue(true)
                }
            } catch (e: Exception) {
                if (e.message?.contains("账号密码不匹配") == true) {
                    register(username, password)
                } else {
                    applyStatus(ViewModelAction.fail(e.message))
                    loginResult.postValue(false)
                }
            }
        }
    }

    private fun register(username: String, password: String) {
        viewModelScope.launch {
            try {
                repository.register(username, password).get()?.let {
                    SPUtils.put(SP_USER, it)
                    applyStatus(ViewModelAction.success())
                    loginResult.postValue(true)
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.message))
                loginResult.postValue(false)
            }
        }
    }
}