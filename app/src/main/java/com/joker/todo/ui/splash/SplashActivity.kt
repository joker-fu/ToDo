package com.joker.todo.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.joker.todo.ui.layout.SplashUI
import org.jetbrains.anko.setContentView

/**
 * SplashActivity
 *
 * @author  joker
 * @date    2019/7/10
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashUI().setContentView(this)
    }

}
