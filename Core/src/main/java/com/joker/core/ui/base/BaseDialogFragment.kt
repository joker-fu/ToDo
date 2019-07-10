package com.joker.core.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.*
import com.joker.core.R
import com.joker.core.utils.JLog

/**
 * BaseDialogFragment
 *
 * @author joker
 * @date 2019/1/18.
 */
abstract class BaseDialogFragment : DialogFragment() {
    private var showTag: String? = null

    protected lateinit var currentActivity: FragmentActivity
    protected lateinit var currentDialog: BaseDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        currentActivity = requireActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置背景透明，才能显示出layout中诸如圆角的布局，否则会有白色底（框）
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BaseDialogStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (!::currentDialog.isInitialized) {
            currentDialog = onCreateBaseDialog(savedInstanceState)
        }
        return currentDialog
    }

    abstract fun onCreateBaseDialog(savedInstanceState: Bundle?): BaseDialog

    fun setDialog(dialog: BaseDialog) {
        this.currentDialog = dialog
    }

    fun show(fragment: Fragment) {
        show(fragment.requireFragmentManager(), "${fragment::class.java.simpleName}${this::class.java.simpleName}")
    }

    fun show(activity: FragmentActivity) {
        show(activity.supportFragmentManager, "${activity::class.java.simpleName}${this::class.java.simpleName}")
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isRepeatShow(tag)) {
            super.show(manager, tag)
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!isRepeatShow(tag)) {
            return super.show(transaction, tag)
        }
        return -1
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        if (!isRepeatShow(tag)) {
            super.showNow(manager, tag)
        }
    }

    private fun isRepeatShow(tag: String?): Boolean {
        val isRepeat = if (tag == null) {
            false
        } else {
            showTag == tag
        }
        showTag = tag
        return isRepeat
    }

    override fun dismiss() {
        try {
            if (dialog?.isShowing == true) {
                super.dismiss()
            }
        } catch (e: Exception) {
            JLog.e(e.toString())
        }
    }

    override fun onDetach() {
        super.onDetach()
        showTag = null
    }
}