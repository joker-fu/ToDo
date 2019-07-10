package com.joker.core.recycler

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.joker.core.recycler.animation.*
import com.joker.core.recycler.constant.AnimationType
import com.joker.core.recycler.util.MultiTypeDelegate


/**
 * BaseListAdapter
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
abstract class BaseListAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {
    abstract val layout: Any

    private val data: MutableList<T> = emptyList<T>().toMutableList()

    private var emptyLayout: FrameLayout? = null
    private var headerLayout: LinearLayout? = null
    private var footerLayout: LinearLayout? = null

    private var isUseEmpty = true
    private var headAndEmptyEnable = false
    private var footAndEmptyEnable = false

    private var openAnimationEnable = true
    private var firstOnlyEnable = false
    private var lastPosition = -1
    private var customAnimation: BaseAnimation? = null
    private var selectAnimation: BaseAnimation = AlphaInAnimation()
    private val interpolator = LinearInterpolator()
    private var duration = 300L

    private var onItemClickListener: ((BaseListAdapter<T>, View, Int) -> Unit)? = null
    private var onItemLongClickListener: ((BaseListAdapter<T>, View, Int) -> Unit)? = null
    private var onItemChildClickListener: ((BaseListAdapter<*>, View, Int) -> Unit)? = null
    private var onItemChildLongClickListener: ((BaseListAdapter<*>, View, Int) -> Boolean)? = null

    private var spanSizeLookup: SpanSizeLookup? = null
    /** 设置header/footer和普通item一致 **/
    private var headerViewAsFlow: Boolean = false
    private var footerViewAsFlow: Boolean = false

    var multiTypeDelegate: MultiTypeDelegate<T>? = null

    fun setHeaderFooterEmpty(isHeadAndEmpty: Boolean, isFootAndEmpty: Boolean) {
        headAndEmptyEnable = isHeadAndEmpty
        footAndEmptyEnable = isFootAndEmpty
    }

    protected fun getEmptyViewCount(): Int = when {
        emptyLayout == null || emptyLayout?.childCount == 0 || !isUseEmpty || data.size > 0 -> 0
        else -> 1
    }

    fun getHeaderLayoutCount(): Int = if (headerLayout == null || headerLayout?.childCount == 0) 0 else 1

    fun getFooterLayoutCount(): Int = if (footerLayout == null || footerLayout?.childCount == 0) 0 else 1

    @JvmOverloads
    fun addHeaderView(header: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (headerLayout == null) {
            headerLayout = LinearLayout(header.context).also {
                it.orientation = orientation
                it.layoutParams = when (orientation) {
                    LinearLayout.VERTICAL -> RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    LinearLayout.HORIZONTAL -> RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    else -> throw java.lang.IllegalArgumentException("orientation must be LinearLayout.VERTICAL or LinearLayout.HORIZONTAL")
                }
            }
        }

        var insertIndex = index
        val childCount = headerLayout!!.childCount
        if (insertIndex < 0 || insertIndex > childCount) {
            insertIndex = childCount
        }
        (header.parent as? ViewGroup)?.removeAllViews()
        headerLayout?.addView(header, insertIndex)
        if (headerLayout?.childCount == 1) {
            val position = getHeaderViewPosition()
            if (position != -1) notifyItemInserted(position)
        }
        return insertIndex
    }

    fun removeHeaderView(header: View) {
        if (getHeaderLayoutCount() == 0) return
        headerLayout?.removeView(header)
        if (headerLayout?.childCount == 0) {
            val position = getHeaderViewPosition()
            if (position != -1) notifyItemRemoved(position)
        }
    }

    @JvmOverloads
    fun setHeaderView(header: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (headerLayout == null || headerLayout!!.childCount <= index) {
            addHeaderView(header, index, orientation)
        } else {
            headerLayout?.removeViewAt(index)
            headerLayout?.addView(header, index)
            index
        }
    }

    fun getHeaderLayout(): LinearLayout? {
        return headerLayout
    }

    @JvmOverloads
    fun addFooterView(footer: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL): Int {
        if (footerLayout == null) {
            footerLayout = LinearLayout(footer.context).also {
                it.orientation = orientation
                it.layoutParams = when (orientation) {
                    LinearLayout.VERTICAL -> RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    LinearLayout.HORIZONTAL -> RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    else -> throw java.lang.IllegalArgumentException("orientation must be LinearLayout.VERTICAL or LinearLayout.HORIZONTAL")
                }
            }
        }

        var insertIndex = index
        val childCount = footerLayout!!.childCount
        if (insertIndex < 0 || insertIndex > childCount) {
            insertIndex = childCount
        }
        (footer.parent as? ViewGroup)?.removeAllViews()
        footerLayout?.addView(footer, insertIndex)
        if (footerLayout?.childCount == 1) {
            val position = getFooterViewPosition()
            if (position != -1) notifyItemInserted(position)
        }
        return insertIndex
    }

    fun removeFooterView(footer: View) {
        if (getHeaderLayoutCount() == 0) return
        footerLayout?.removeView(footer)
        if (footerLayout?.childCount == 0) {
            val position = getFooterViewPosition()
            if (position != -1) notifyItemRemoved(position)
        }
    }

    @JvmOverloads
    fun setFooterView(footer: View, index: Int = 0, orientation: Int = LinearLayout.VERTICAL): Int {
        return if (footerLayout == null || footerLayout!!.childCount <= index) {
            addFooterView(footer, index, orientation)
        } else {
            footerLayout?.removeViewAt(index)
            footerLayout?.addView(footer, index)
            index
        }
    }

    fun getFooterLayout(): LinearLayout? {
        return footerLayout
    }

    fun setEmptyView(@LayoutRes resId: Int, viewGroup: ViewGroup) {
        val view = LayoutInflater.from(viewGroup.context).inflate(resId, null, false)
        setEmptyView(view)
    }

    fun setEmptyView(empty: View) {
        var insert = false
        if (emptyLayout == null) {
            emptyLayout = FrameLayout(empty.context).also {
                val lp = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.MATCH_PARENT
                )
                empty.layoutParams?.apply {
                    lp.width = width
                    lp.height = height
                }
                it.layoutParams = lp
                insert = true
            }
        }
        emptyLayout?.removeAllViews()
        emptyLayout?.addView(empty)
        isUseEmpty = true
        if (insert && getEmptyViewCount() == 1) {
            var position = 0
            if (headAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++
            }
            notifyItemInserted(position)
        }
    }

    fun getEmptyLayout(): FrameLayout? {
        return emptyLayout
    }

    private fun getHeaderViewPosition(): Int {
        if (getEmptyViewCount() == 1) {
            if (headAndEmptyEnable) {
                return 0
            }
        } else {
            return 0
        }
        return -1
    }

    private fun getFooterViewPosition(): Int {
        if (getEmptyViewCount() == 1) {
            var position = 1
            if (headAndEmptyEnable && getHeaderLayoutCount() != 0) {
                position++
            }
            if (footAndEmptyEnable) {
                return position
            }
        } else {
            return getHeaderLayoutCount() + data.size
        }
        return -1
    }

    /** 获取真实position 去除header **/
    fun getRealPosition(position: Int): Int {
        return position - getHeaderLayoutCount()
    }

    fun set(element: T, @IntRange(from = 0) index: Int) {
        data.set(index, element)
        notifyItemChanged(index + getHeaderLayoutCount())
    }


    /** 插入数据 **/
    @JvmOverloads
    fun add(element: T, @IntRange(from = 0) index: Int = data.size) {
        data.add(index, element)
        notifyItemInserted(data.size + getHeaderLayoutCount())
        compatibilityDataSizeChanged(1)
    }

    /** 插入数据 **/
    @JvmOverloads
    fun addAll(elements: Collection<T>?, @IntRange(from = 0) index: Int = data.size) {
        elements?.let {
            data.addAll(index, it)
            notifyItemRangeInserted(data.size - elements.size + getHeaderLayoutCount(), data.size)
        }
        compatibilityDataSizeChanged(elements?.size)
    }

    /** 移除数据 **/
    fun remove(element: T) {
        val index = data.indexOf(element)
        if (index != -1) removeAt(index)
    }

    /** 移除数据 **/
    fun removeAt(@IntRange(from = 0) index: Int) {
        data.removeAt(index)
        val realPosition = getRealPosition(index)
        notifyItemRemoved(realPosition)
        compatibilityDataSizeChanged(0)
        notifyItemRangeChanged(realPosition, data.size - realPosition)
    }

    /** 清除数据 **/
    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    /** 重置数据 **/
    fun replace(element: T, @IntRange(from = 0) index: Int) {
        data[index] = element
        val realPosition = getRealPosition(index)
        notifyItemChanged(realPosition)
    }

    /** 重置数据 **/
    fun replaceAll(elements: Collection<T>?) {
        if (data != elements) {
            data.clear()
            data.addAll(elements ?: mutableListOf())
            notifyDataSetChanged()
        }
    }

    /** 获取数据 **/
    fun getItem(@IntRange(from = 0) index: Int): T? {
        return data.getOrNull(index)
    }

    /** 获取数据 **/
    fun getData(): MutableList<T> {
        return data
    }

    private fun compatibilityDataSizeChanged(size: Int?) {
        val dataSize = if (data.isEmpty()) 0 else data.size
        if (dataSize == size) {
            notifyDataSetChanged()
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        val type = holder.itemViewType
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW) {
            setFullSpan(holder)
        } else {
            addAnimation(holder)
        }
    }

    protected fun setFullSpan(holder: BaseViewHolder) {
        if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val params = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            params.isFullSpan = true
        }
    }

    private fun addAnimation(holder: BaseViewHolder) {
        if (openAnimationEnable) {
            if (!firstOnlyEnable || holder.layoutPosition > lastPosition) {
                val animation: BaseAnimation? = customAnimation ?: selectAnimation
                for (anim in animation!!.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.layoutPosition)
                }
                lastPosition = holder.layoutPosition
            }
        }
    }

    protected fun startAnim(anim: Animator, index: Int) {
        anim.setDuration(duration).start()
        anim.interpolator = interpolator
    }

    fun openLoadAnimation(animationType: AnimationType) {
        this.openAnimationEnable = true
        customAnimation = null
        selectAnimation = when (animationType) {
            AnimationType.ALPHAIN -> AlphaInAnimation()
            AnimationType.SCALEIN -> ScaleInAnimation()
            AnimationType.SLIDEIN_BOTTOM -> SlideInBottomAnimation()
            AnimationType.SLIDEIN_LEFT -> SlideInLeftAnimation()
            AnimationType.SLIDEIN_RIGHT -> SlideInRightAnimation()
        }
    }

    fun openLoadAnimation(animation: BaseAnimation) {
        this.openAnimationEnable = true
        this.customAnimation = animation
    }

    fun openLoadAnimation() {
        this.openAnimationEnable = true
    }

    fun closeLoadAnimation() {
        this.openAnimationEnable = false
    }

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    fun isFirstOnly(firstOnly: Boolean) {
        this.firstOnlyEnable = firstOnly
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val lm = recyclerView.layoutManager
        if (lm is GridLayoutManager) {
            lm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val type = getItemViewType(position)
                    if (type == HEADER_VIEW && isHeaderViewAsFlow()) {
                        return 1
                    }
                    if (type == FOOTER_VIEW && isFooterViewAsFlow()) {
                        return 1
                    }
                    return if (spanSizeLookup == null) {
                        if (isFixedViewType(type)) lm.spanCount else 1
                    } else {
                        if (isFixedViewType(type))
                            lm.spanCount
                        else
                            spanSizeLookup!!.getSpanSize(lm, getRealPosition(position))
                    }
                }
            }
        }
    }

    protected open fun isFixedViewType(type: Int): Boolean {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW
    }

    fun setSpanSizeLookup(spanSizeLookup: SpanSizeLookup) {
        this.spanSizeLookup = spanSizeLookup
    }

    fun setHeaderViewAsFlow(headerViewAsFlow: Boolean) {
        this.headerViewAsFlow = headerViewAsFlow
    }

    fun isHeaderViewAsFlow(): Boolean {
        return headerViewAsFlow
    }

    fun setFooterViewAsFlow(footerViewAsFlow: Boolean) {
        this.footerViewAsFlow = footerViewAsFlow
    }

    fun isFooterViewAsFlow(): Boolean {
        return footerViewAsFlow
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = when (viewType) {
            EMPTY_VIEW -> emptyLayout
            HEADER_VIEW -> headerLayout
            FOOTER_VIEW -> footerLayout
            else -> onCreateItemView(parent, viewType)
        } ?: throw IllegalArgumentException("itemView is null ! ")

        return BaseViewHolder(itemView).also {
            it.bindAdapter(this)
            if (viewType !in intArrayOf(EMPTY_VIEW, HEADER_VIEW, FOOTER_VIEW)) {
                bindViewClickListener(it)
            }
        }
    }

    open fun onCreateItemView(parent: ViewGroup, viewType: Int): View? {
        val layout = multiTypeDelegate?.getLayoutId(viewType) ?: layout
        return when (layout) {
            is Int -> LayoutInflater.from(parent.context).inflate(layout, parent, false)
            is View -> layout
            else -> null
        }
    }

    override fun getItemCount(): Int {
        var count: Int
        if (getEmptyViewCount() == 1) {
            count = 1
            if (headAndEmptyEnable && getHeaderLayoutCount() != 0) {
                count++
            }
            if (footAndEmptyEnable && getFooterLayoutCount() != 0) {
                count++
            }
        } else {
            count = getHeaderLayoutCount() + data.size + getFooterLayoutCount()
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        if (getEmptyViewCount() == 1) {
            val hasHeader = headAndEmptyEnable && getHeaderLayoutCount() > 0
            return when (position) {
                0 -> if (hasHeader) HEADER_VIEW else EMPTY_VIEW
                1 -> if (hasHeader) EMPTY_VIEW else FOOTER_VIEW
                2 -> FOOTER_VIEW
                else -> EMPTY_VIEW
            }
        }

        val headerCount = getHeaderLayoutCount()
        return when {
            position < headerCount -> HEADER_VIEW
            position - headerCount < data.size -> getDefItemViewType(getRealPosition(position))
            else -> FOOTER_VIEW
        }
    }

    open fun getDefItemViewType(position: Int): Int {
        val type = multiTypeDelegate?.getDefItemViewType(data, position)
        return type ?: super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            HEADER_VIEW, FOOTER_VIEW, EMPTY_VIEW -> {
            }
            else -> {
                val realPosition = getRealPosition(position)
                convert(holder, getItem(realPosition), realPosition)
            }
        }
    }

    abstract fun convert(holder: BaseViewHolder, item: T?, position: Int)

    private fun bindViewClickListener(vh: BaseViewHolder) {
        getOnItemClickListener()?.let { itemClickListener ->
            vh.itemView.setOnClickListener {
                itemClickListener.invoke(this, it, vh.layoutPosition - getHeaderLayoutCount())
            }
        }
        getOnItemLongClickListener()?.let { itemLongClickListener ->
            vh.itemView.setOnClickListener {
                itemLongClickListener.invoke(this, it, vh.layoutPosition - getHeaderLayoutCount())
            }
        }
    }

    fun setOnItemClickListener(listener: (adapter: BaseListAdapter<T>, view: View, position: Int) -> Unit) {
        onItemClickListener = listener
    }

    fun getOnItemLongClickListener(): ((BaseListAdapter<T>, View, Int) -> Unit)? {
        return onItemClickListener
    }

    fun setOnItemLongClickListener(listener: (adapter: BaseListAdapter<T>, view: View, position: Int) -> Unit) {
        onItemLongClickListener = listener
    }

    fun getOnItemClickListener(): ((BaseListAdapter<T>, View, Int) -> Unit)? {
        return onItemLongClickListener
    }

    fun setOnItemChildClickListener(listener: (adapter: BaseListAdapter<*>, view: View, position: Int) -> Unit) {
        onItemChildClickListener = listener
    }

    fun getOnItemChildClickListener(): ((BaseListAdapter<*>, View, Int) -> Unit)? {
        return onItemChildClickListener
    }

    fun setOnItemChildLongClickListener(listener: (adapter: BaseListAdapter<*>, view: View, position: Int) -> Boolean) {
        onItemChildLongClickListener = listener
    }

    fun getOnItemChildLongClickListener(): ((BaseListAdapter<*>, View, Int) -> Boolean)? {
        return onItemChildLongClickListener
    }

    interface SpanSizeLookup {
        /**
         * @param gridLayoutManager gridLayoutManager
         * @param position position
         */
        fun getSpanSize(gridLayoutManager: GridLayoutManager, position: Int): Int
    }

    companion object {
        const val EMPTY_VIEW = 0x00000111
        const val HEADER_VIEW = 0x00000222
        const val FOOTER_VIEW = 0x00000333
    }
}


