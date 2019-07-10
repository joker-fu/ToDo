package com.joker.todo.model.bean

/**
 * ToDoPage
 *
 * @author  joker
 * @date    2019/7/10
 */
data class ToDoPage(
    val curPage: Int,
    val datas: List<Inventory>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)