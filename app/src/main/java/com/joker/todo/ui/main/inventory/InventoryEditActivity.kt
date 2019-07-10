package com.joker.todo.ui.main.inventory

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.joker.core.ext.*
import com.joker.core.ui.base.BaseActivity
import com.joker.todo.R
import com.joker.todo.model.bean.Inventory
import com.joker.todo.viewmodel.InventoryVM
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_inventory.*
import java.util.*


/**
 * InventoryEditActivity
 *
 * @author  joker
 * @date    2019/7/10
 */
class InventoryEditActivity : BaseActivity<InventoryVM>(), DatePickerDialog.OnDateSetListener {

    override val layout: Any
        get() = R.layout.activity_inventory

    private val inventory by lazy {
        getIntentParams<Inventory>("PARAMS_BEAN")
    }

    override fun onBindView(savedInstanceState: Bundle?) {
        tvDate.apply {
            text = System.currentTimeMillis().toDateString(PATTERN_yyyyMMdd)
            setOnClickListener {
                val now = Calendar.getInstance()
                DatePickerDialog.newInstance(
                    this@InventoryEditActivity,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
                ).show(supportFragmentManager, "DatePickerDialog")
            }
        }
        titleBar.rightTextView.setOnClickListener {
            val title = etTitle.trimString()
            val content = etContent.trimString()
            val date = tvDate.trimString()
            if (inventory != null) {
                inventory?.apply {
                    viewModel.update(id, title, content, date, status, type, priority)
                }
            } else {
                viewModel.insert(title, content, date)
            }
        }
        initData()
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        tvDate.text = "$year-${monthOfYear + 1}-$dayOfMonth"
    }

    private fun initData() {
        if (inventory != null) {
            etTitle.setText(inventory?.title)
            etContent.setText(inventory?.content)
            tvDate.text = inventory?.dateStr
        }
        viewModel.inventory.observe(this) {
            toast(R.string.operate_success)
            setResult(Activity.RESULT_OK, Intent().also { intent ->
                intent.putExtra("PARAMS_BEAN", it)
            })
            finish()
        }
    }

    companion object {
        const val REQUEST_CODE = 0x11

        fun launch(fragment: Fragment, inventory: Inventory? = null) {
            val intent = Intent(fragment.context, InventoryEditActivity::class.java)
            intent.putExtra("PARAMS_BEAN", inventory)
            fragment.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}