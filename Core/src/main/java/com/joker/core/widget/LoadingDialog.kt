package com.joker.core.widget

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.joker.core.R
import com.joker.core.ext.visible
import com.joker.core.ui.base.BaseDialog
import com.joker.core.ui.base.BaseDialogFragment
import com.joker.core.utils.JLog
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * LoadingDialog
 *
 * @author joker
 * @date 2019/1/18.
 */
class LoadingDialog : BaseDialogFragment() {

    private var message: CharSequence? = null

    override fun onCreateBaseDialog(savedInstanceState: Bundle?): BaseDialog {
        return BaseDialog(currentActivity, R.style.LoadingDialogStyle).apply {
            setContentView(R.layout.dialog_loading)
            tvLoadingLabel.text = message
            if (!message.isNullOrEmpty()) {
                tvLoadingLabel.visible()
            }
        }
    }

    fun setMessage(resId: Int): LoadingDialog {
        return setMessage(currentActivity.getText(resId))
    }

    fun setMessage(charSequence: CharSequence?): LoadingDialog {
        message = charSequence
        tvLoadingLabel?.text = message
        if (!message.isNullOrEmpty()) {
            tvLoadingLabel?.visible()
        }
        return this
    }

    class Builder(context: Context) {

        private val bContext: Context = context
        private var charSequence: CharSequence? = null

        fun setMessage(resId: Int?): Builder {
            try {
                resId?.let {
                    setMessage(bContext.getText(it))
                }
            } catch (e: Exception) {
                JLog.e(e.toString())
            }
            return this
        }

        fun setMessage(charSequence: CharSequence?): Builder {
            this.charSequence = charSequence
            return this
        }

        fun create(): LoadingDialog {
            return LoadingDialog().apply {
                val dialog = BaseDialog(bContext, R.style.LoadingDialogStyle)
                dialog.apply {
                    setContentView(R.layout.dialog_loading)
                    tvLoadingLabel.text = charSequence
                    if (!charSequence.isNullOrEmpty()) {
                        tvLoadingLabel.visible()
                    }
                }
                setDialog(dialog)
            }
        }

        fun show(fragment: Fragment): LoadingDialog {
            val loadingDialog = create()
            loadingDialog.show(fragment)
            return loadingDialog
        }

        fun show(activity: FragmentActivity): LoadingDialog {
            val loadingDialog = create()
            loadingDialog.show(activity)
            return loadingDialog
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): LoadingDialog {
            return LoadingDialog()
        }
    }

}