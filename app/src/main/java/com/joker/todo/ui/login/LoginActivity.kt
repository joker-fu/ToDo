package com.joker.todo.ui.login

import android.os.Bundle
import com.joker.core.ext.click
import com.joker.core.ext.observe
import com.joker.core.ext.trimString
import com.joker.core.ui.base.BaseActivity
import com.joker.core.utils.SPUtils
import com.joker.todo.R
import com.joker.todo.common.SP_USER
import com.joker.todo.model.bean.User
import com.joker.todo.ui.main.MainActivity
import com.joker.todo.viewmodel.LoginVM
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

/**
 * LoginActivity
 *
 * @author  joker
 * @date    2019/7/10
 */
class LoginActivity : BaseActivity<LoginVM>() {

    override val layout: Any
        get() = R.layout.activity_login

    override fun onBindView(savedInstanceState: Bundle?) {
        val user = SPUtils.get<User>(SP_USER)
        if (user != null) {
            toNext()
        } else {
            loginBtn.click {
                val username = etUsername.trimString()
                val password = etPassword.trimString()
                viewModel.login(username, password)
            }
        }
        viewModel.loginResult.observe(this) {
            if (it) toNext()
        }
    }

    private fun toNext() {
        startActivity<MainActivity>()
        finish()
    }

}