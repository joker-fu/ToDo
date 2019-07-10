package com.joker.core.ui.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.gyf.immersionbar.ImmersionBar
import com.joker.core.R
import com.joker.core.common.AppManager
import com.joker.core.ext.hideLoading
import com.joker.core.ext.showLoading
import com.joker.core.ext.toast
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.viewmodel.BaseViewModel
import com.joker.core.widget.LoadingDialog
import java.lang.reflect.ParameterizedType


/**
 * Activity
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseActivity<VM : ViewModel> : AppCompatActivity() {

    /** activity layout id or view **/
    protected abstract val layout: Any
    protected lateinit var currentActivity: FragmentActivity
        private set
    /** 允许快速点击重复启动 默认true **/
    protected open val isAllowRepeatedStart = true
    private var startActivityTag: String? = null
    private var startActivityTime = 0L
    /** LoadingDialog **/
    var loadingDialog: LoadingDialog? = null
    /** 使用沉浸式 **/
    protected open val useImmersionBar: Boolean = true
    /** 全屏模式 **/
    protected open val isFullScreen: Boolean = false

    protected lateinit var viewModel: VM

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentActivity = this
        initViewModel()
        beforeSetContentView()
        when (layout) {
            is Int -> setContentView(layout as Int)
            is View -> setContentView(layout as View)
            else -> throw IllegalArgumentException("layout must be a layout id or view! ")
        }
        // 沉浸式状态栏
        if (useImmersionBar) {
            initImmersionBar()
        }
        onBindView(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun initViewModel() {
        val clz = try {
            var res: Class<VM>? = null
            var c: Class<*> = javaClass
            while (c != BaseActivity::class.java) {
                if (c.genericSuperclass is ParameterizedType)
                    res = (c.genericSuperclass as ParameterizedType).actualTypeArguments[0] as? Class<VM>
                if (res != null) break
                c = c.superclass
            }
            res
        } catch (e: Exception) {
            null
        }
        clz?.let {
            viewModel = ViewModelProviders.of(this).get(it)
            observeAction()
        }
    }

    protected fun isInitViewModel(): Boolean {
        return ::viewModel.isInitialized
    }

    protected open fun observeAction() {
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
                    toast(it.message)
                }
                ViewModelAction.ACTION_TOAST -> {
                    toast(it.message)
                }
                else -> {
                    throw RuntimeException("ViewModelAction State Error!")
                }
            }
        })
    }

    protected fun beforeSetContentView() {
        // activity放到application栈中管理
        AppManager.manager.addActivity(this)
        // 全屏模式
        if (isFullScreen) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        // 无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // 默认不自动弹起软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    protected open fun initImmersionBar() {
        ImmersionBar.with(this).reset()
        ImmersionBar.with(this)
                .fitsSystemWindows(true)  //使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.colorPrimary)
                .autoDarkModeEnable(true, 0.2f)
                .autoNavigationBarDarkModeEnable(true, 0.2f)
                .keyboardEnable(true)
                .init()
    }

    protected abstract fun onBindView(savedInstanceState: Bundle?)

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        AppManager.manager.removeActivity(this)
    }

    override fun finish() {
        // 隐藏软键盘，避免软键盘引发的内存泄露
        currentFocus?.let {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            manager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
        super.finish()
    }

    @CallSuper
    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (checkCanStart(intent)) {
            super.startActivityForResult(intent, requestCode, options)
        }
    }

    /** 检测防止快速点击重复启动 **/
    private fun checkCanStart(intent: Intent?): Boolean {
        if (isAllowRepeatedStart || intent == null) {
            return true
        } else {
            val tag = when {
                intent.component != null -> intent.component!!.className
                intent.action != null -> intent.action
                else -> return true
            }
            if (tag == startActivityTag && startActivityTime >= SystemClock.uptimeMillis() - 500) {
                return false
            }
            startActivityTag = tag
            startActivityTime = SystemClock.uptimeMillis()
        }
        return true
    }
}