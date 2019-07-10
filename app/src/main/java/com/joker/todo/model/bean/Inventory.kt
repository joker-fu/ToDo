package com.joker.todo.model.bean

import java.io.Serializable

/**
 * Inventory
 *
 * @author  joker
 * @date    2019/7/10
 */
data class Inventory(
    val completeDate: Any,
    val completeDateStr: String,
    val content: String,
    val date: Long,
    val dateStr: String,
    val id: Long,
    val priority: Int,
    var status: Int,
    val title: String,
    val type: Int,
    val userId: Int
) : Serializable {
    fun isComplete(): Boolean {
        return status == 1
    }
}