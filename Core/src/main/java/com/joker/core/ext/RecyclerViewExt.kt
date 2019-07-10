package com.joker.core.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joker.core.recycler.decoration.DividerItemDecoration

/**
 * RecyclerViewExt
 *
 * @author joker
 * @date 2019/6/17.
 */
/*------------------------- RecyclerView  扩展 -------------------------*/

fun RecyclerView.config(
        ad: RecyclerView.Adapter<*>,
        lm: RecyclerView.LayoutManager? = null,
        itemDecoration: RecyclerView.ItemDecoration? = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
) {
    layoutManager = lm ?: LinearLayoutManager(context)
    itemDecoration?.let {
        addItemDecoration(it)
    }
    adapter = ad
}