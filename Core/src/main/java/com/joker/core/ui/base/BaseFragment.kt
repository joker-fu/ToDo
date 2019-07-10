package com.joker.core.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.joker.core.ext.hideLoading
import com.joker.core.ext.showLoading
import com.joker.core.ext.toast
import com.joker.core.recycler.bean.ViewModelAction
import com.joker.core.viewmodel.BaseViewModel
import com.joker.core.widget.LoadingDialog
import java.lang.reflect.ParameterizedType

/**
 * BaseFragment
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseFragment<VM : ViewModel> : Fragment() {

    /** fragment layout id or view **/
    protected abstract val layout: Any
    protected lateinit var currentActivity: FragmentActivity
        private set
    protected var savedInstanceState: Bundle? = null
    /** 懒加载 **/
    protected open val useLazyLoadMode = true
    protected var isLoadData = false
    protected var isViewCreated = false
    /** LoadingDialog **/
    var loadingDialog: LoadingDialog? = null

    protected lateinit var viewModel: VM

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentActivity = requireActivity()
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (layout) {
            is Int -> inflater.inflate(layout as Int, container, false)
            is View -> layout as View
            else -> throw IllegalArgumentException("layout must be a layout id or view! ")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewCreated = true
        this.savedInstanceState = savedInstanceState
        initViewModel()
        check2bind()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        check2bind()
    }

    private fun check2bind() {
        if (useLazyLoadMode) {
            if (userVisibleHint && isViewCreated) {
                if (!isLoadData) {
                    onBindView(view, savedInstanceState)
                    isLoadData = true
                }
            }
        } else if (isViewCreated) {
            onBindView(view, savedInstanceState)
        }
    }

    /** bindView 懒加载时只调用1次 **/
    abstract fun onBindView(view: View?, savedInstanceState: Bundle?)

    @Suppress("UNCHECKED_CAST", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun initViewModel() {
        val clz = try {
            var res: Class<VM>? = null
            var c: Class<*> = javaClass
            while (c != BaseFragment::class.java) {
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

    /** 销毁当前 Fragment 所在的 Activity **/
    fun finish() {
        currentActivity.finish()
    }

}