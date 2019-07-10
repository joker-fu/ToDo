package com.joker.core.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joker.core.recycler.entity.SectionEntity

/**
 * BaseSectionListAdapter
 *
 * @author joker
 * @date 2019/1/20.
 */
abstract class BaseSectionListAdapter<T : SectionEntity<*>> : BaseListAdapter<T>() {

    abstract val sectionHeadLayout: Any

    override fun onCreateItemView(parent: ViewGroup, viewType: Int): View? {
        val curLayout = if (SECTION_HEADER_VIEW == viewType) sectionHeadLayout else layout
        return when (curLayout) {
            is Int -> LayoutInflater.from(parent.context).inflate(curLayout, parent, false)
            is View -> curLayout
            else -> null
        }
    }

    override fun getDefItemViewType(position: Int): Int {
        return if (getItem(position)?.isHeader == true) SECTION_HEADER_VIEW else super.getDefItemViewType(position)
    }

    protected override fun isFixedViewType(type: Int): Boolean {
        return super.isFixedViewType(type) || type == SECTION_HEADER_VIEW
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            SECTION_HEADER_VIEW -> {
                val realPosition = getRealPosition(position)
                onConvertHead(holder, getItem(realPosition), position)
            }
            else -> super.onBindViewHolder(holder, position)
        }
    }

    abstract fun onConvertHead(holder: BaseViewHolder, item: T?, position: Int)

    companion object {
        protected const val SECTION_HEADER_VIEW = -0xee
    }
}