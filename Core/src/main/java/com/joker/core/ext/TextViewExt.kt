package com.joker.core.ext

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.DrawableRes

/**
 * TextViewExt
 *
 * @author joker
 * @date 2019/1/18.
 */

/*------------------------- TextView  text 扩展 -------------------------*/
fun TextView.string(): String {
    return text.toString()
}

fun TextView.trimString(): String {
    return text.toString().trim()
}

/*------------------------- TextView  TextChangedListener 扩展 -------------------------*/

inline fun TextView.textWatcher(init: JTextWatcher.() -> Unit): TextWatcher {
    val watcher = JTextWatcher().apply(init)
    addTextChangedListener(watcher)
    return watcher
}

class JTextWatcher : TextWatcher {

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((Editable?) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable) {
        _afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    fun afterChanged(listener: (Editable?) -> Unit) {
        _afterTextChanged = listener
    }

}

/*------------------------- TextView drawable 扩展 -------------------------*/

fun TextView.drawableLeft(@DrawableRes id: Int, width: Int? = null, height: Int? = width) {
    val rect = Rect(0, 0, width ?: getWidth(), height ?: getHeight())
    val d = drawable(id).apply { bounds = rect }
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(d, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
}

fun TextView.drawableTop(@DrawableRes id: Int, width: Int? = null, height: Int? = width) {
    val rect = Rect(0, 0, width ?: getWidth(), height ?: getHeight())
    val d = drawable(id).apply { bounds = rect }
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(compoundDrawables[0], d, compoundDrawables[2], compoundDrawables[3])
}

fun TextView.drawableRight(@DrawableRes id: Int, width: Int? = null, height: Int? = width) {
    val rect = Rect(0, 0, width ?: getWidth(), height ?: getHeight())
    val d = drawable(id).apply { bounds = rect }
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], d, compoundDrawables[3])
}

fun TextView.drawableBottom(@DrawableRes id: Int, width: Int? = null, height: Int? = width) {
    val rect = Rect(0, 0, width ?: getWidth(), height ?: getHeight())
    val d = drawable(id).apply { bounds = rect }
    d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
    this.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], d)
}

fun TextView.clearDrawable() {
    this.setCompoundDrawables(null, null, null, null)
}

/*------------------------- TextView span 扩展 -------------------------*/

fun TextView.scaleSpan(str: String = "", range: IntRange, scale: Float = 1f): TextView {
    text = (if (str.isEmpty()) text else str).toScaleSpan(range, scale)
    return this
}

fun TextView.appendScaleSpan(str: String = "", scale: Float = 1f): TextView {
    append(str.toScaleSpan(0..str.length, scale))
    return this
}

fun TextView.colorSpan(str: String = "", range: IntRange, color: Int = Color.RED): TextView {
    text = (if (str.isEmpty()) text else str).toColorSpan(range, color)
    return this
}

fun TextView.appendColorSpan(str: String = "", color: Int = Color.RED): TextView {
    append(str.toColorSpan(0..str.length, color))
    return this
}

fun TextView.backgroundColorSpan(str: String = "", range: IntRange, color: Int = Color.RED): TextView {
    text = (if (str.isEmpty()) text else str).toBackgroundColorSpan(range, color)
    return this
}

fun TextView.appendBackgroundColorSpan(str: String = "", color: Int = Color.RED): TextView {
    append(str.toBackgroundColorSpan(0..str.length, color))
    return this
}

fun TextView.strikeThrougthSpan(str: String = "", range: IntRange): TextView {
    text = (if (str.isEmpty()) text else str).toStrikeThroughSpan(range)
    return this
}

fun TextView.appendStrikeThrougthSpan(str: String = ""): TextView {
    append(str.toStrikeThroughSpan(0..str.length))
    return this
}

fun TextView.clickSpan(
        str: String = "", range: IntRange,
        color: Int = Color.RED, isUnderlineText: Boolean = false, clickAction: () -> Unit
): TextView {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT  // remove click bg color
    text = (if (str.isEmpty()) text else str).toClickSpan(range, color, isUnderlineText, clickAction)
    return this
}

fun TextView.appendClickSpan(
        str: String = "", color: Int = Color.RED,
        isUnderlineText: Boolean = false, clickAction: () -> Unit
): TextView {
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT  // remove click bg color
    append(str.toClickSpan(0..str.length, color, isUnderlineText, clickAction))
    return this
}

fun TextView.styleSpan(str: String = "", range: IntRange, style: Int = Typeface.BOLD): TextView {
    text = (if (str.isEmpty()) text else str).toStyleSpan(style = style, range = range)
    return this
}

fun TextView.appendStyleSpan(str: String = "", style: Int = Typeface.BOLD): TextView {
    append(str.toStyleSpan(style = style, range = 0..str.length))
    return this
}

fun TextView.imageSpan(
        str: String = "", range: IntRange,
        @DrawableRes drawable: Int
): TextView {
    val bitmap = BitmapFactory.decodeResource(resources, drawable)
    text = (if (str.isEmpty()) text else str).toImageSpan(context, bitmap, range)
    return this
}

fun TextView.appendImageSpan(@DrawableRes drawable: Int): TextView {
    val str = "*"
    val bitmap = BitmapFactory.decodeResource(resources, drawable)
    append(str.toImageSpan(context, bitmap, 0..str.length))
    return this
}
