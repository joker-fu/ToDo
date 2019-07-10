package com.joker.core.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * CommonExt
 *
 * @author joker
 * @date 2019/6/15.
 */

/** 动态创建Drawable
 * @param radiusII size == 1 设置4个圆角；
 *                 size == 2 radiusII[0] 左、右上圆角  radiusII[1] 左、右下圆角 ；
 *                 size == 4 左上、右上、左下、右下圆角 ；
 *                 其他不支持 ；
 **/
fun Context.createDrawable(
        color: Int = Color.TRANSPARENT,
        radiusII: Array<Float>? = null,
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        enableRipple: Boolean = true,
        rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    val content = GradientDrawable().apply {
        setColor(color)
//        radius?.let { cornerRadius = it }
        radiusII?.let {
            cornerRadii = when (it.size) {
                1 -> floatArrayOf(it[0], it[0], it[0], it[0], it[0], it[0], it[0], it[0])
                2 -> floatArrayOf(it[0], it[0], it[0], it[0], it[1], it[1], it[1], it[1])
                4 -> floatArrayOf(it[0], it[0], it[1], it[1], it[2], it[2], it[3], it[3])
                else -> throw IllegalArgumentException("radiusII size 不得大于4")
            }
        }
        setStroke(strokeWidth, strokeColor)
    }
    if (Build.VERSION.SDK_INT >= 21 && enableRipple) {
        return RippleDrawable(ColorStateList.valueOf(rippleColor), content, null)
    }
    return content
}

fun Fragment.createDrawable(
        color: Int = Color.TRANSPARENT,
        radiusII: Array<Float>? = null,
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        enableRipple: Boolean = true,
        rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(color, radiusII, strokeColor, strokeWidth, enableRipple, rippleColor)
}

fun View.createDrawable(
        color: Int = Color.TRANSPARENT,
        radiusII: Array<Float>? = null,
        strokeColor: Int = Color.TRANSPARENT,
        strokeWidth: Int = 0,
        enableRipple: Boolean = true,
        rippleColor: Int = Color.parseColor("#88999999")
): Drawable {
    return context!!.createDrawable(color, radiusII, strokeColor, strokeWidth, enableRipple, rippleColor)
}

/** json相关 **/
fun Any.toJson() = Gson().toJson(this)

inline fun <reified T> String.toBean(): T = Gson().fromJson<T>(this, object : TypeToken<T>() {}.type)

/** 屏幕相关 **/
fun Context.screenWidth(): Int = resources.displayMetrics.widthPixels

fun Context.screenHeight(): Int = resources.displayMetrics.heightPixels

fun Fragment.screenWidth(): Int = context!!.screenWidth()

fun Fragment.screenHeight(): Int = context!!.screenHeight()

fun View.screenWidth(): Int = context!!.screenWidth()

fun View.screenHeight(): Int = context!!.screenHeight()

fun Any.runOnUIThread(action: () -> Unit) {
    Handler(Looper.getMainLooper()).post { action() }
}
