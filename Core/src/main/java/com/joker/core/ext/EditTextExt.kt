package com.joker.core.ext

import android.text.InputType
import android.text.method.KeyListener
import android.widget.EditText
import com.joker.core.R

/**
 * EditTextExt
 *
 * @author joker
 * @date 2019/1/18.
 */

//禁用EditText编辑 注：如果要点击监听 则监听 Touch 的 Down 事件
fun EditText.editable(bool: Boolean) {
    if (bool) {
        keyListener = getTag(R.id.tagFirstId) as? KeyListener
        inputType = getTag(R.id.tagSecondId) as Int
    } else {
        setTag(R.id.tagFirstId, keyListener)
        setTag(R.id.tagSecondId, inputType)
        keyListener = null
        inputType = InputType.TYPE_NULL
    }
    setTextIsSelectable(bool)
    isClickable = bool
    isLongClickable = bool
    isCursorVisible = bool
    setTextIsSelectable(bool)
}