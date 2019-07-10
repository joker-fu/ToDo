package com.joker.core.ui.base

import android.app.Dialog
import android.content.Context
import android.view.View
import com.joker.core.R

/**
 * BaseDialog
 *
 * @author joker
 * @date 2019/1/18.
 */
class BaseDialog : Dialog {

    constructor(context: Context) : this(context, R.style.BaseDialogStyle)

    constructor(context: Context, theme: Int) : super(context, theme)

    abstract class Builder {
        abstract val layout: Any

        protected var context: Context
        private var theme: Int = 0
        lateinit var dialog: BaseDialog

        constructor(context: Context) : this(context, 0)

        constructor(context: Context, theme: Int) {
            this.context = context
            this.theme = theme
            initialize()
        }

        private fun initialize() {
            dialog = if (theme == 0) BaseDialog(context) else BaseDialog(
                    context,
                    theme
            )

            when (layout) {
                is Int -> dialog.setContentView(layout as Int)
                is View -> dialog.setContentView(layout as View)
                else -> throw IllegalArgumentException("layout must be a layout id or view! ")
            }

            initDialog(dialog)
        }

        abstract fun initDialog(dialog: BaseDialog)

//        open fun show() {
//            dialog.show()
//        }
//
//        open fun dismiss() {
//            dialog.dismiss()
//        }
    }
}