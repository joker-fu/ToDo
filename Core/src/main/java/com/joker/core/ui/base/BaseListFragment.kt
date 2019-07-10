package com.joker.core.ui.base

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joker.core.R
import com.joker.core.common.LIST_FIRST_PAGE
import com.joker.core.common.LIST_PAGE_SIZE
import com.joker.core.ext.*
import com.joker.core.recycler.BaseListAdapter
import com.joker.core.recycler.BaseViewHolder
import com.joker.core.recycler.MultiDelegateAdapter
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.recycler.decoration.DividerItemDecoration
import com.joker.core.utils.JLog
import com.joker.core.viewmodel.BaseViewModel
import com.joker.core.widget.TitleBar
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import kotlinx.android.synthetic.main.fragment_base_list.*

/**
 * BaseFragment
 *
 * @author joker
 * @date 2019/1/18.
 */
abstract class BaseListFragment<VM : ViewModel, T> : BaseFragment<VM>() {

    override val layout: Any
        get() = R.layout.fragment_base_list

    protected var pageIndex = LIST_FIRST_PAGE

    protected lateinit var listAdapter: BaseListAdapter<T>
    protected lateinit var listLayoutManager: RecyclerView.LayoutManager
    protected lateinit var listItemDecoration: RecyclerView.ItemDecoration
    protected open val itemDecorationPadding = 0
    protected lateinit var emptyView: View

    @CallSuper
    override fun onBindView(view: View?, savedInstanceState: Bundle?) {
        initView()
        initListParams()
        initTopTitleBar(titleBar)
        initListTop(flTop)
        initSmartRefreshLayout(smartRefreshLayout)
        initSmartRefreshLoadMore(smartRefreshLayout)
        initRecyclerView(recyclerView)
        initListBottom(flBottom)
    }

    private fun initView() {
        emptyView = View.inflate(currentActivity, R.layout.core_layout_empty_view, null)
    }

    private fun initListParams() {
        listAdapter = MultiDelegateAdapter<T>().also { la ->
            createItemTypeParams().apply {
                layoutId?.let { la.registerItemType(layoutId) }
                        ?: la.registerItemType(*layoutParams)
            }
            la.setViewBinder { holder, item, position ->
                itemBinder(holder, item, position)
            }
            la.setOnItemClickListener { adapter, view, position ->
                itemClick(adapter, view, position)
            }
            la.setOnItemLongClickListener { adapter, view, position ->
                itemLongClick(adapter, view, position)
            }
        }
        listLayoutManager = LinearLayoutManager(context)
        listItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL, itemDecorationPadding)
    }

    protected open fun initTopTitleBar(titleBar: TitleBar?) {
        titleBar?.visible()
    }

    protected open fun initListTop(flTop: FrameLayout?) = Unit

    protected open fun initSmartRefreshLayout(smartRefreshLayout: SmartRefreshLayout?) {
        smartRefreshLayout?.apply {
            autoRefresh(0, 200, 1F, false)
            setEnableRefresh(true)
            setOnRefreshListener {
                when (it.state) {
                    RefreshState.Refreshing -> onRefresh(it)
                    else -> JLog.i(it.toString())
                }
            }
        }
    }

    protected open fun initSmartRefreshLoadMore(smartRefreshLayout: SmartRefreshLayout?) {
        smartRefreshLayout?.apply {
            setEnableLoadMore(true)
            setOnLoadMoreListener {
                when (it.state) {
                    RefreshState.Loading -> onLoadMore(it)
                    else -> JLog.i(it.toString())
                }
            }
        }
    }

    fun finishRefresh() {
        smartRefreshLayout?.finishRefresh()
    }

    fun finishLoadMore() {
        smartRefreshLayout?.finishLoadMore(100)
    }

    protected open fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.config(listAdapter, listLayoutManager, listItemDecoration)
    }

    protected open fun initListBottom(flBottom: FrameLayout?) = Unit

    fun addAll(elements: Collection<T>?, @IntRange(from = 0) index: Int = listAdapter.getData().size) {
        if (pageIndex == LIST_FIRST_PAGE) {
            listAdapter.replaceAll(elements)
            if (elements.isNullOrEmpty()) {
                listAdapter.setEmptyView(emptyView)
            }
        } else {
            listAdapter.addAll(elements, index)
        }
        finishRefresh()
        finishLoadMore()
        if (elements.isNullOrEmpty() || elements.size < LIST_PAGE_SIZE) {
            smartRefreshLayout.postDelayed({
                //fixme smartRefreshLayout bug 1.1.0-andx-11已修复 但是没有底部没有更多数据回弹效果
                smartRefreshLayout.finishLoadMoreWithNoMoreData()
            }, 100)
        }
    }

    override fun observeAction() {
        if (!isInitViewModel()) return
        (viewModel as? BaseViewModel<*>)?.viewModelAction?.observe(this, Observer {
            when (it.state) {
                ViewModelAction.ACTION_LOADING -> {
                    showLoading(it.message)
                }
                ViewModelAction.ACTION_IDLE,
                ViewModelAction.ACTION_FAIL,
                ViewModelAction.ACTION_SUCCESS,
                ViewModelAction.ACTION_COMPLETE -> {
                    hideLoading()
                    finishRefresh()
                    finishLoadMore()
                    toast(it.message)
                }
                ViewModelAction.ACTION_TOAST -> {
                    toast(it.message)
                }
            }
        })
    }

    open fun onRefresh(refreshLayout: RefreshLayout) {
        pageIndex = LIST_FIRST_PAGE
        loadData(pageIndex)
    }

    open fun onLoadMore(refreshLayout: RefreshLayout) {
        pageIndex++
        loadData(pageIndex)
    }

    /** 创建列表布局类型参数 **/
    abstract fun createItemTypeParams(): ItemTypeParams

    /** 加载列表数据 **/
    abstract fun loadData(pageIndex: Int)

    /** 绑定列表数据 **/
    abstract fun itemBinder(holder: BaseViewHolder, item: T?, position: Int)

    open fun itemClick(adapter: BaseListAdapter<T>, view: View, position: Int) = Unit

    open fun itemLongClick(adapter: BaseListAdapter<T>, view: View, position: Int) = Unit

    class ItemTypeParams(@LayoutRes val layoutId: Int?) {

        var layoutParams: Array<out Pair<Int, Int>> = arrayOf()

        constructor(vararg layoutParams: Pair<Int, Int>) : this(null) {
            this.layoutParams = layoutParams
        }
    }
}