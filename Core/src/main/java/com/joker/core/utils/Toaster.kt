package com.joker.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toaster
 *
 * @author joker
 * @date 2019/1/18.
 */

object Toaster {

    private var mToast: Toast? = null

    fun showToast(context: Context, @StringRes resId: Int, delay: Int = Toast.LENGTH_SHORT) {
        showToast(context, context.getString(resId), delay)
    }

    @SuppressLint("ShowToast")
    fun showToast(context: Context, text: CharSequence, delay: Int = Toast.LENGTH_SHORT) {
        if (mToast == null) {
            mToast = Toast.makeText(context.applicationContext, text, delay)
        }
        with(mToast!!) {
            setText(text)
            duration = delay
            show()
        }
    }

}