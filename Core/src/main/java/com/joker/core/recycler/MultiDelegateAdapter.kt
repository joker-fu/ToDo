package com.joker.core.recycler

import androidx.annotation.LayoutRes
import com.joker.core.recycler.constant.DEFAULT_VIEW_TYPE
import com.joker.core.recycler.entity.MultiItemEntity
import com.joker.core.recycler.util.MultiTypeDelegate

/**
 * MultiDelegateAdapter
 *
 * @author joker
 * @date 2019/1/20.
 */
class MultiDelegateAdapter<T> : BaseListAdapter<T>() {

    override val layout: Any
        get() = 0

    private lateinit var binder: (holder: BaseViewHolder, item: T?, position: Int) -> Unit

    init {
        //此处可以不用再具体实现中重置
        //不一定要继承 MultiItemEntity
        multiTypeDelegate = object : MultiTypeDelegate<T>() {
            override fun getItemType(t: T): Int {
                return if (t is MultiItemEntity) {
                    t.getItemType()
                } else {
                    DEFAULT_VIEW_TYPE
                }
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: T?, position: Int) {
        if (::binder.isInitialized) {
            binder(holder, item, position)
        } else {
            throw RuntimeException("MultiDelegateAdapter binder is not init")
        }
    }

    fun registerItemType(vararg params: Pair<Int, Int>) {
        params.asSequence().forEach {
            multiTypeDelegate?.registerItemType(it.first, it.second)
        }
    }

    fun registerItemType(@LayoutRes layoutResId: Int) {
        multiTypeDelegate?.registerItemType(DEFAULT_VIEW_TYPE, layoutResId)
    }

    fun setViewBinder(block: (holder: BaseViewHolder, item: T?, position: Int) -> Unit) {
        binder = block
    }

}