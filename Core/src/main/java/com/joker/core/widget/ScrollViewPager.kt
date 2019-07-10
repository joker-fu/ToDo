package com.joker.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * ScrollViewPager
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ScrollViewPager : ViewPager {

    var isCanScroll = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return isCanScroll && super.onTouchEvent(ev)
    }
}