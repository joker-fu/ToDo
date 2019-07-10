package com.joker.todo.model.service


import com.joker.core.http.HttpResponse
import com.joker.todo.common.*
import com.joker.todo.model.bean.Inventory
import com.joker.todo.model.bean.ToDoPage
import com.joker.todo.model.bean.User
import retrofit2.http.*

/**
 * TodoService
 *
 * @author  joker
 * @date    2019/7/10
 */
interface TodoService {

    @POST(TODO_REGISTER)
    @FormUrlEncoded
    suspend fun register(
        @Field("username") account: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): HttpResponse<User>

    @POST(TODO_LOGIN)
    @FormUrlEncoded
    suspend fun login(
        @Field("username") account: String,
        @Field("password") password: String
    ): HttpResponse<User>

    @GET(TODO_LOGOUT)
    suspend fun logout(): HttpResponse<Any>

    @GET(TODO_PAGE_DATA)
    suspend fun getInventories(@Path("pageIndex") pageIndex: Int): HttpResponse<ToDoPage>

    @POST(TODO_INSERT)
    @FormUrlEncoded
    suspend fun insert(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): HttpResponse<Inventory>


    @POST(TODO_UPDATE)
    @FormUrlEncoded
    suspend fun update(
        @Path("id") id: Long?,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("status") status: Int?,
        @Field("type") type: Int?,
        @Field("priority") priority: Int?
    ): HttpResponse<Inventory>

    @POST(TODO_DONE)
    @FormUrlEncoded
    suspend fun updateStatus(
        @Path("id") id: Long,
        @Field("status") status: Int
    ): HttpResponse<Inventory>

    @POST(TODO_DELETE)
    suspend fun delete(
        @Path("id") id: Long
    ): HttpResponse<Any>
}
