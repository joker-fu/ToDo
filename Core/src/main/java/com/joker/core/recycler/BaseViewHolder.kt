package com.joker.core.recycler

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView


/**
 * BaseViewHolder
 *
 * @author joker
 * @date 2019/1/18.
 */
class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    lateinit var adapter: BaseListAdapter<*>

    private var views: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> findView(@IdRes id: Int): T? {
        var view = views.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            views.put(id, view)
        }
        return view as? T
    }

    internal fun bindAdapter(listAdapter: BaseListAdapter<*>) {
        adapter = listAdapter
    }

    fun addOnClickListener(@IdRes vararg ids: Int) {
        ids.asSequence().forEach { id ->
            findView<View>(id)?.let { view ->
                if (!view.isClickable) {
                    view.isClickable = true
                }
                view.setOnClickListener { v ->
                    adapter.getOnItemChildClickListener()?.invoke(adapter, v, getClickPosition())
                }
            }
        }
    }

    fun addOnLongClickListener(@IdRes vararg ids: Int) {
        ids.asSequence().forEach { id ->
            findView<View>(id)?.let { view ->
                if (!view.isClickable) {
                    view.isClickable = true
                }
                view.setOnLongClickListener { v ->
                    adapter.getOnItemChildLongClickListener()?.invoke(adapter, v, getClickPosition()) ?: false
                }
            }
        }
    }

    private fun getClickPosition(): Int {
        return if (layoutPosition >= adapter.getHeaderLayoutCount()) {
            layoutPosition - adapter.getHeaderLayoutCount()
        } else 0
    }


}
