package com.joker.core.ext

import android.app.Activity
import android.view.View
import androidx.annotation.StringRes
import com.joker.core.widget.TitleBar


/**
 * TitleBarExt.kt
 *
 * @author joker
 * @date 2019/1/25.
 */

/**
 * @param center 居中标题
 * @param leftClick 左侧点击事件 默认点击返回
 */
inline fun TitleBar.common(center: String?, crossinline leftClick: (View) -> Unit = {
    if (it.context is Activity) {
        (it.context as? Activity)?.onBackPressed()
    }
}) {
    centerTextView?.text = center
    leftImageButton?.click { leftClick.invoke(it) }
}

/**
 * @param center 居中标题id
 * @param leftClick 左侧点击事件 默认点击返回
 */
inline fun TitleBar.common(@StringRes center: Int, crossinline leftClick: (View) -> Unit = {
    if (it.context is Activity) {
        (it.context as? Activity)?.onBackPressed()
    }
}) {
    centerTextView?.text = string(center)
    leftImageButton?.click { leftClick.invoke(it) }
}