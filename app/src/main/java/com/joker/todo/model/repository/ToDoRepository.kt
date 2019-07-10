package com.joker.todo.model.repository


import com.joker.core.http.HttpResponse
import com.joker.core.http.NetworkHelper
import com.joker.core.http.create
import com.joker.core.model.repository.BaseRepository
import com.joker.todo.model.service.TodoService
import com.joker.todo.model.bean.Inventory
import com.joker.todo.model.bean.ToDoPage
import com.joker.todo.model.bean.User

/**
 * ToDoRepository
 *
 * @author  joker
 * @date    2019/7/10
 */
class ToDoRepository : BaseRepository<TodoService>() {

    override val service: TodoService
        get() = NetworkHelper.getDefault().create()

    suspend fun login(username: String, password: String): HttpResponse<User> {
        return service.login(username, password)
    }

    suspend fun register(username: String, password: String): HttpResponse<User> {
        return service.register(username, password, password)
    }

    suspend fun getInventories(pageIndex: Int): HttpResponse<ToDoPage> {
        return service.getInventories(pageIndex)
    }

    suspend fun insert(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): HttpResponse<Inventory> {
        return service.insert(title, content, date, type, priority)
    }

    suspend fun update(
        id: Long?,
        title: String,
        content: String,
        date: String,
        status: Int?,
        type: Int?,
        priority: Int?
    ): HttpResponse<Inventory> {
        return service.update(id, title, content, date, status, type, priority)
    }

    suspend fun updateStatus(
        id: Long,
        status: Int
    ): HttpResponse<Inventory> {
        return service.updateStatus(id, status)
    }

    suspend fun delete(
        id: Long
    ): HttpResponse<Any> {
        return service.delete(id)
    }
}
