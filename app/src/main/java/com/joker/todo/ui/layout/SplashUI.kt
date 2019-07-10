package com.joker.todo.ui.layout

import android.view.Gravity
import android.view.View
import android.widget.Button
import com.joker.core.ext.background
import com.joker.core.ext.color
import com.joker.todo.ui.login.LoginActivity
import com.joker.todo.ui.splash.SplashActivity
import com.joker.todo.R
import org.jetbrains.anko.*

/**
 * SplashUI
 *
 * @author  joker
 * @date    2019/7/10
*/
class SplashUI : AnkoComponent<SplashActivity> {

    override fun createView(ui: AnkoContext<SplashActivity>) = with(ui) {

        lateinit var btn: Button

        verticalLayout {
            setBackgroundColor(color(R.color.white))
            lparams(matchParent, matchParent)
            textView {
                setText(R.string.app_name)
                textColor = R.color.black_grace
                textSize = 60f
                gravity = Gravity.CENTER
                scaleX = 0f
                animate().apply {
                    duration = 1500
                    scaleX(0.8f)
                    start()
                }
            }.lparams(matchParent, wrapContent)
            imageView {
                setImageResource(R.mipmap.ic_launcher)
                padding = dip(100)
                alpha = 0f
                scaleX = 0f
                scaleY = 0f
                animate().apply {
                    duration = 1500
                    alpha(1f)
                    scaleX(1f)
                    scaleY(1f)
                    withEndAction {
                        btn.performClick()
                    }
                    start()
                }
            }.lparams(matchParent, dip(100)) {
                weight = 1f
            }
            btn = button {
                visibility = View.GONE
                text = "进入主页"
                background(R.color.grey, arrayOf(10f))
                setOnClickListener {
                    startActivity<LoginActivity>()
                    owner.finish()
                }
            }.lparams(matchParent, wrapContent) {
                margin = dip(16)
            }
        }
    }

}
