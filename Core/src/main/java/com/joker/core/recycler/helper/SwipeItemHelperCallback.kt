package com.joker.core.recycler.helper

import android.graphics.Canvas
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.color.MaterialColors.ALPHA_FULL


/**
 * SwipeItemHelperCallback
 *
 * @author  joker
 * @date    2019/1/29
 */
class SwipeItemHelperCallback : ItemTouchHelper.Callback() {

    private var adapter: RecyclerView.Adapter<*>? = null

    /** item支持长按进入拖动操作 **/
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    /** item支持滑动操作 **/
    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 1.5f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 100
    }

    /** 指定可以支持的拖放和滑动的方向，上下为拖动（drag），左右为滑动（swipe） **/
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (adapter == null) {
            adapter = recyclerView.adapter
        }
        val layoutManager = recyclerView.layoutManager
        return if (layoutManager is GridLayoutManager || layoutManager is StaggeredGridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        } else {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }
    }

    /** 拖动操作 **/
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }
        (adapter as? ItemTouchListener)?.let {
            it.onItemDrag(viewHolder.adapterPosition, target.adapterPosition)
        }
        return true
    }

    /** 滑动操作 **/
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (adapter as? ItemTouchListener)?.let {
            it.onItemSwipe(viewHolder.adapterPosition)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            if (Math.abs(dX) <= 160) {
//                viewHolder.itemView.scrollTo(-dX.toInt(), 0)
//            }else{
            //自定义滑动动画
            val alpha = ALPHA_FULL - Math.abs(dX) / viewHolder.itemView.width
            viewHolder.itemView.alpha = alpha
            viewHolder.itemView.translationX = dX
//            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }


    }

    /** 静止变为拖动/滑动状态会回调该方法 **/
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
    }

    /** 操作完毕会回调该方法 **/
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        ItemTouchHelper(this).attachToRecyclerView(recyclerView)
    }

    fun getItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(this)
    }
}