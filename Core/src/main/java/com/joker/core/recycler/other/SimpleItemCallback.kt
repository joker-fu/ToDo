package com.joker.core.recycler.other

import androidx.recyclerview.widget.DiffUtil

/**
 * SimpleItemCallback
 *
 * @author joker
 * @date 2019/1/18.
 */
open class SimpleItemCallback<T> : DiffUtil.ItemCallback<T>() {

    //areItemsTheSame返回为false
    // 不论areContentsTheSame是什么，adapter中的条目都会更新
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}