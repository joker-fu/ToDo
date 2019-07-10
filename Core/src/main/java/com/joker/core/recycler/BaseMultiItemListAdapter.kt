package com.joker.core.recycler

import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.joker.core.recycler.constant.DEFAULT_VIEW_TYPE
import com.joker.core.recycler.constant.ITEM_TYPE_NOT_FOUND
import com.joker.core.recycler.entity.MultiItemEntity

/**
 * BaseMultiItemListAdapter
 *
 * @author joker
 * @date 2019/1/20.
 */
abstract class BaseMultiItemListAdapter<T : MultiItemEntity> : BaseListAdapter<T>() {

    private var layoutIds: SparseIntArray? = null

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): View? {
        val layout = getLayoutId(viewType) ?: return null
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }

    override fun getDefItemViewType(position: Int): Int {
        return getItem(position)?.getItemType() ?: DEFAULT_VIEW_TYPE
    }

    @LayoutRes
    private fun getLayoutId(itemType: Int): Int? {
        return layoutIds?.get(itemType, ITEM_TYPE_NOT_FOUND)
    }

    protected fun addItemType(itemType: Int, @LayoutRes layoutId: Int) {
        if (layoutIds == null) {
            layoutIds = SparseIntArray()
        }
        layoutIds?.put(itemType, layoutId)
    }
}