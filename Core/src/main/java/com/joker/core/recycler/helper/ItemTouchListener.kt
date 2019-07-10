package com.joker.core.recycler.helper

/**
 * ItemTouchListener
 *
 * @author  joker
 * @date    2019/1/29
 */
interface ItemTouchListener {

    /** 拖动 **/
    fun onItemDrag(fromPosition: Int, toPosition: Int)

    /** 滑动 **/
    fun onItemSwipe(position: Int)
}