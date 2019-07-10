package com.joker.todo.model.bean

/**
 * User
 *
 * @author  joker
 * @date    2019/7/10
 */
data class User(
    var chapterTops: List<Any> = listOf(),
    var collectIds: List<Any> = listOf(),
    var email: String = "",
    var icon: String = "",
    var id: Long = 0, // 16643
    var password: String = "",
    var token: String = "",
    var type: Int = 0, // 0
    var username: String = "" // fugui0604
)
