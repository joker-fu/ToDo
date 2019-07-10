package com.joker.todo.ui.main.inventory

import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.joker.core.ext.*
import com.joker.core.recycler.BaseListAdapter
import com.joker.core.recycler.BaseViewHolder
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.ui.base.BaseListFragment
import com.joker.core.utils.SPUtils
import com.joker.core.widget.SwipeMenuLayout
import com.joker.todo.R
import com.joker.todo.common.IS_SHOW_INVENTORY_CONTENT
import com.joker.todo.model.bean.Inventory
import com.joker.todo.viewmodel.CenterVM
import com.joker.todo.viewmodel.InventoryVM
import kotlinx.android.synthetic.main.fragment_inventories.*
import kotlinx.android.synthetic.main.item_inventory_layout.view.*

/**
 * InventoriesFragment
 *
 * @author  joker
 * @date    2019/7/10
 */
class InventoriesFragment : BaseListFragment<InventoryVM, Inventory>() {

    override val layout: Any
        get() = R.layout.fragment_inventories

    private var isShowContent: Boolean = false
    private val centerVM by lazy {
        createViewModelByActivity<CenterVM>()
    }

    override fun onBindView(view: View?, savedInstanceState: Bundle?) {
        super.onBindView(view, savedInstanceState)
        fabNewInventory.click {
            InventoryEditActivity.launch(this)
        }
        bindData()
    }

    override fun createItemTypeParams(): ItemTypeParams {
        return ItemTypeParams(R.layout.item_inventory_layout)
    }

    override fun loadData(pageIndex: Int) {
        viewModel.getInventories(pageIndex)
    }

    private fun bindData() {
        isShowContent = SPUtils.get(IS_SHOW_INVENTORY_CONTENT) ?: false
        centerVM.isShowContent.postValue(isShowContent)

        viewModel.viewModelAction.observe(this) {
            when (it.state) {
                ViewModelAction.ACTION_LOADING -> {
                    showLoading()
                }
                ViewModelAction.ACTION_SUCCESS, ViewModelAction.ACTION_FAIL -> {
                    finishRefresh()
                    finishLoadMore()
                    hideLoading()
                }
            }
        }
        viewModel.inventories.observe(this) {
            addAll(it)
        }
        centerVM.isShowContent.observe(this) {
            isShowContent = it
            listAdapter.notifyDataSetChanged()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun itemBinder(holder: BaseViewHolder, item: Inventory?, position: Int) {
        holder.itemView.apply {
            tvTitle.text = item?.title
            tvDesc.text = item?.content
            tvDate.text = item?.dateStr

            val flag = if (item?.isComplete() == true) Paint.STRIKE_THRU_TEXT_FLAG else 0
            tvTitle.paint.flags = flag
            tvDesc.paint.flags = flag
            if (isShowContent) tvDesc.visible() else tvDesc.gone()

            holder.addOnClickListener(R.id.llItem)

            swipeMenuLayout.setOnSwipeListener { scrollX, status ->
                if (item == null) return@setOnSwipeListener
                if (scrollX >= 0) {
                    tvRight.visible()
                    tvLeft.gone()
                    val color = if (scrollX > 160) R.color.red else R.color.grey_light
                    tvRight.setBackgroundResource(color)
                } else {
                    tvRight.gone()
                    tvLeft.visible()
                    val color = if (scrollX < -160 && !item.isComplete()) R.color.green else R.color.grey_light
                    val textRes = if (item.isComplete()) R.string.cancel_completed else R.string.completed
                    tvLeft.setText(textRes)
                    tvLeft.setBackgroundResource(color)
                }

                llItem.elevation = when (status) {
                    SwipeMenuLayout.Status.MOVING -> {
                        10f
                    }
                    SwipeMenuLayout.Status.OPEN -> {
                        if (scrollX < 0) {
                            //完成
                            val state = if (item.isComplete()) 0 else 1
                            viewModel.updateStatus(item.id, state) {
                                listAdapter.set(it, position)
                            }
                        } else {
                            //删除
                            viewModel.delete(item.id) {
                                listAdapter.removeAt(position)
                            }
                        }
                        0f
                    }
                    else -> {
                        0f
                    }
                }
            }
        }
    }

    override fun itemClick(adapter: BaseListAdapter<Inventory>, view: View, position: Int) {
        InventoryEditActivity.launch(this, adapter.getData()[position] as? Inventory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == InventoryEditActivity.REQUEST_CODE) {
            val inventory = data?.getSerializableExtra("PARAMS_BEAN") as? Inventory ?: return
            val list = listAdapter.getData()
            list.add(inventory)
            list.sortByDescending { it.dateStr }
            listAdapter.notifyDataSetChanged()
        }
    }

}