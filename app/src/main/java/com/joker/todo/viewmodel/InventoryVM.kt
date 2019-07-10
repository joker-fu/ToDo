package com.joker.todo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.joker.core.ext.wrap
import com.joker.core.http.get
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.viewmodel.BaseViewModel
import com.joker.todo.model.bean.Inventory
import com.joker.todo.model.repository.ToDoRepository
import kotlinx.coroutines.launch

/**
 * InventoryVM
 *
 * @author  joker
 * @date    2019/7/10
 */
class InventoryVM : BaseViewModel<ToDoRepository>() {

    override val repository: ToDoRepository
        get() = ToDoRepository()

    val inventories by lazy {
        MutableLiveData<List<Inventory>>()
    }

    val inventory by lazy {
        MutableLiveData<Inventory>()
    }

    fun getInventories(pageIndex: Int) {
        viewModelScope.launch {
            try {
                repository.getInventories(pageIndex + 1).get().let {
                    applyStatus(ViewModelAction.success())
                    inventories.postValue(it?.datas)
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.wrap().message))
            }
        }
    }

    fun insert(title: String, content: String, date: String, type: Int = 0, priority: Int = 0) {
        viewModelScope.launch {
            try {
                repository.insert(title, content, date, type, priority).get().let {
                    inventory.postValue(it)
                    applyStatus(ViewModelAction.success())
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.wrap().message))
            }
        }
    }

    fun update(id: Long?, title: String, content: String, date: String, status: Int?, type: Int?, priority: Int?) {
        viewModelScope.launch {
            try {
                repository.update(id, title, content, date, status, type, priority).get().let {
                    inventory.postValue(it)
                    applyStatus(ViewModelAction.success())
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.wrap().message))
            }
        }
    }

    fun updateStatus(id: Long, status: Int, onSuccess: (Inventory) -> Unit) {
        viewModelScope.launch {
            try {
                repository.updateStatus(id, status).get().let {
                    applyStatus(ViewModelAction.success())
                    it?.let { res -> onSuccess(res) }
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.wrap().message))
            }
        }
    }

    fun delete(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.delete(id).get().let {
                    applyStatus(ViewModelAction.success())
                    onSuccess()
                }
            } catch (e: Exception) {
                applyStatus(ViewModelAction.fail(e.wrap().message))
            }
        }
    }
}