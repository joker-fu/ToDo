package com.joker.core.ext

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.joker.core.R
import org.jetbrains.anko.dip

/**
 * ViewExt
 *
 * @author joker
 * @date 2019/1/18.
 */

/**
 * 设置 View 宽高
 * @param width 要设置的宽度
 * @param height 要设置的高度
 */
fun View.widthHeight(width: Int? = null, height: Int? = null): View {
    layoutParams = (layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )).apply {
        this.width = width ?: this.width
        this.height = height ?: this.height
    }
    return this
}

fun View.minWidthHeight(width: Int? = null, height: Int? = null): View {
    width?.let { minimumWidth = width }
    height?.let { minimumHeight = height }
    return this
}

/**
 * 设置 View 宽，带过渡动画
 * @param targetValue 目标宽度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.widthByAnimate(targetValue: Int, duration: Long = 400, action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(width, targetValue).apply {
            addUpdateListener {
                widthHeight(width = it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置 View 高，带过渡动画
 * @param targetValue 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.heightByAnimate(targetValue: Int, duration: Long = 400, action: ((Float) -> Unit)? = null) {
    post {
        ValueAnimator.ofInt(height, targetValue).apply {
            addUpdateListener {
                widthHeight(height = it.animatedValue as Int)
                action?.invoke((it.animatedFraction))
            }
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置 View 宽高，带过渡动画
 * @param targetWidth 目标宽度
 * @param targetHeight 目标高度
 * @param duration 时长
 * @param action 可选行为
 */
fun View.widthHeightByAnimate(
        targetWidth: Int,
        targetHeight: Int,
        duration: Long = 400,
        action: ((Float) -> Unit)? = null
) {
    post {
        val startHeight = height
        val evaluator = IntEvaluator()
        ValueAnimator.ofInt(width, targetWidth).apply {
            addUpdateListener {
                widthHeight(
                        it.animatedValue as Int,
                        evaluator.evaluate(it.animatedFraction, startHeight, targetHeight)
                )
                action?.invoke((it.animatedFraction))
            }
            setDuration(duration)
            start()
        }
    }
}

/**
 * 设置 View margin
 * @param left 默认保留原来的
 * @param top 默认是保留原来的
 * @param right 默认是保留原来的
 * @param bottom 默认是保留原来的
 */
fun View.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null): View {
    layoutParams = (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        if (left != null) leftMargin = left
        if (top != null) topMargin = top
        if (right != null) rightMargin = right
        if (bottom != null) bottomMargin = bottom
    }
    return this
}

// 获取View截图, 获取整个RecyclerView列表截图
fun View.toBitmap(): Bitmap {
    if (measuredWidth == 0 || measuredHeight == 0) {
        throw RuntimeException("view to bitmap error，measuredWidth：$measuredWidth measuredHeight: $measuredHeight")
    }
    return when (this) {
        is RecyclerView -> {
            this.scrollToPosition(0)
            this.measure(
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val bitmap = Bitmap.createBitmap(width, measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            //draw default bg, otherwise will be black
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            this.draw(canvas)
            //恢复高度
            this.measure(
                    View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
            )
            bitmap
        }
        else -> {
            val screenshot = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(screenshot)
            if (background != null) {
                background.setBounds(0, 0, width, measuredHeight)
                background.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)
            screenshot
        }
    }
}

// ViewGroup 子View
inline val ViewGroup.children
    get() = (0 until childCount).map { getChildAt(it) }

/*------------------------- view 点击事件 扩展 -------------------------*/

private var viewClickFlag = false
private var clickRunnable = Runnable { viewClickFlag = false }

fun View.clickPost(action: (v: View) -> Unit) {
    setOnClickListener {
        if (!viewClickFlag) {
            viewClickFlag = true
            action(it)
            removeCallbacks(clickRunnable)
            postDelayed(clickRunnable, 300)
        } else {
            action(it)
        }
    }
}

fun View.click(action: (v: View) -> Unit) {
    setOnClickListener {
        action(it)
    }
}

fun kotlin.Any.clicks(vararg views: View, action: (v: View) -> Unit) {
    views.forEach { v ->
        v.setOnClickListener {
            action(it)
        }
    }
}

fun View.longClick(action: (v: View) -> Boolean) {
    setOnLongClickListener {
        action(it)
    }
}

fun kotlin.Any.longClicks(vararg views: View, action: (v: View) -> Unit) {
    views.forEach { v ->
        v.setOnClickListener {
            action(it)
        }
    }
}

/*------------------------- view 显示隐藏 扩展 -------------------------*/

fun View.gone() {
    this.visibility = View.GONE
}

fun View.isGone(): Boolean {
    return this.visibility == View.GONE
}

fun kotlin.Any.gones(vararg views: View) {
    views.forEach {
        it.gone()
    }
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.isInvisible(): Boolean {
    return this.visibility == View.INVISIBLE
}

fun kotlin.Any.invisibles(vararg views: View) {
    views.forEach {
        it.invisible()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.isVisible(): Boolean {
    return this.visibility == View.VISIBLE
}

fun View.toggleVisibility() {
    this.visibility = if (this.visibility == View.GONE) View.VISIBLE else View.GONE
}

fun kotlin.Any.visibles(vararg views: View) {
    views.forEach {
        it.visible()
    }
}

fun View.showSoftKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (imm != null) {
        requestFocus()
        imm.showSoftInput(this, 0)
    }
}

fun View.hideSoftKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/*------------------------- view shape 扩展 -------------------------*/

/**
 * @param radiusIIDp size == 1 设置4个圆角；
 *                 size == 2 radiusII[0] 左、右上圆角  radiusII[1] 左、右下圆角 ；
 *                 size == 4 左上、右上、左下、右下圆角 ；
 *                 其他不支持 ；
 */
fun View.background(@ColorRes colorRes: Int = android.R.color.transparent,
                    radiusIIDp: Array<Float>? = null,
                    @ColorRes strokeColorRes: Int = android.R.color.transparent,
                    strokeWidthDp: Int = 0,
                    enableRipple: Boolean = true,
                    @ColorRes rippleColorRes: Int = R.color.ripple_color) {
    background = createDrawable(color(colorRes), radiusIIDp?.map { dip(it).toFloat() }?.toTypedArray(), color(strokeColorRes), dip(strokeWidthDp), enableRipple, color(rippleColorRes))
}

//fun View.setShape(
//        solidColor: String? = null,
//        radius: Float? = null,
//        strokeWidth: Int? = null,
//        strokeColor: String? = null,
//        radiusArray: FloatArray? = null
//) {
//    this.background = GradientDrawable().apply {
//        gradientType = GradientDrawable.RECTANGLE
//        solidColor?.let {
//            setColor(Color.parseColor(it))
//        }
//        radius?.let {
//            cornerRadius = it
//        }
//        radiusArray?.let {
//            cornerRadii = it
//        }
//        strokeWidth?.let {
//            setStroke(it, Color.parseColor(strokeColor))
//        }
//    }
//}

//fun View.setShape(
//        solidColor: Int? = null,
//        radius: Float? = null,
//        strokeWidth: Int? = null,
//        strokeColor: Int? = null,
//        radiusArray: FloatArray? = null
//) {
//    this.background = GradientDrawable().apply {
//        gradientType = GradientDrawable.RECTANGLE
//        solidColor?.let { solidColor ->
//            setColor(context.color(solidColor))
//        }
//        radius?.let {
//            cornerRadius = it
//        }
//        radiusArray?.let {
//            cornerRadii = it
//        }
//        strokeWidth?.let {
//            strokeColor?.let { strokeColor ->
//                setStroke(it, context.color(strokeColor))
//            }
//        }
//    }
//}

/*------------------------- view selector 扩展 -------------------------*/

//fun View.setSelector(
//        stateSet0: IntArray,
//        drawable0: Drawable,
//        stateSet1: IntArray,
//        drawable1: Drawable
//) {
//    this.background = StateListDrawable().apply {
//        addState(stateSet0, drawable0)
//        addState(stateSet1, drawable1)
//    }
//}