package com.joker.todo.model.repository


import com.joker.core.http.HttpResponse
import com.joker.core.http.NetworkHelper
import com.joker.core.http.create
import com.joker.core.model.repository.BaseRepository
import com.joker.todo.model.service.TodoService

/**
 * CenterRepository
 *
 * @author  joker
 * @date    2019/7/10
 */
class CenterRepository : BaseRepository<TodoService>() {

    override val service: TodoService
        get() = NetworkHelper.getDefault().create()

    suspend fun logout(): HttpResponse<Any> {
        return service.logout()
    }

}
