package com.joker.todo.ui.main.center


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.joker.core.common.AppManager
import com.joker.core.ext.click
import com.joker.core.ext.createViewModelByActivity
import com.joker.core.ext.observe
import com.joker.core.ui.SimpleFragment
import com.joker.core.utils.SPUtils
import com.joker.todo.R
import com.joker.todo.common.IS_SHOW_INVENTORY_CONTENT
import com.joker.todo.common.SP_USER
import com.joker.todo.model.bean.User
import com.joker.todo.ui.login.LoginActivity
import com.joker.todo.viewmodel.CenterVM
import kotlinx.android.synthetic.main.fragment_center.*

/**
 * CenterFragment
 *
 * @author  joker
 * @date    2019/7/10
 */
class CenterFragment : SimpleFragment() {
    override val layout: Any
        get() = R.layout.fragment_center

    private val centerVM by lazy {
        createViewModelByActivity<CenterVM>()
    }

    override fun onBindView(view: View?, savedInstanceState: Bundle?) {
        tvLogout.click {
            centerVM.logout()
            AppManager.manager.removeOthersActivity(currentActivity::class.java)
            startActivity(Intent(currentActivity, LoginActivity::class.java))
            finish()
        }
        showContentSwitch.setOnCheckedChangeListener { _, isChecked ->
            SPUtils.put(IS_SHOW_INVENTORY_CONTENT, isChecked)
            centerVM.isShowContent.postValue(isChecked)
        }
        val user = SPUtils.get<User>(SP_USER)
        titleBar.leftTextView.text = user?.username
        centerVM.isShowContent.observe(this) {
            showContentSwitch.isChecked = it
        }
    }

}
