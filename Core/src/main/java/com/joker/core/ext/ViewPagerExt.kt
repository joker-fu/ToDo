package com.joker.core.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import org.jetbrains.anko.dip

/**
 * ViewPagerExt
 *
 * @author joker
 * @date 2019/6/15.
 */

/**
 * 给ViewPager绑定数据
 */
fun ViewPager.bind(
    count: Int,
    bindView: (container: ViewGroup, position: Int) -> View
): ViewPager {
    adapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, any: Any) = view == any
        override fun getCount() = count
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = bindView(container, position)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
    return this
}

/**
 * 给ViewPager绑定Fragment
 */
fun ViewPager.bind(
    fm: FragmentManager,
    vararg fragments: Pair<String, Fragment>
): ViewPager {
    adapter = object : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int) = fragments[position].second
        override fun getCount() = fragments.size
        override fun getPageTitle(position: Int) = fragments[position].first
    }
    return this
}

/**
 * 让ViewPager展示卡片效果
 * @param pageMargin 用来调节卡片之间的距离
 * @param padding 用来调节ViewPager的padding
 */
fun ViewPager.asCard(
    pageMargin: Int = 30,
    padding: Int = 45
): ViewPager {
    setPageTransformer(false, CardPagerTransformer(context))
    setPageMargin(dip(pageMargin))
    clipToPadding = false
    dip(padding).let {
        setPadding(it, it, it, it)
    }
    return this
}

private class CardPagerTransformer(context: Context) : ViewPager.PageTransformer {
    private val maxTranslateOffsetX: Int = context.dip(180f)
    private var viewPager: ViewPager? = null

    override fun transformPage(view: View, position: Float) {
        if (viewPager == null) {
            viewPager = view.parent as ViewPager
        }
        val leftInScreen = view.left - viewPager!!.scrollX
        val centerXInViewPager = leftInScreen + view.measuredWidth / 2
        val offsetX = centerXInViewPager - viewPager!!.measuredWidth / 2
        val offsetRate = offsetX.toFloat() * 0.38f / viewPager!!.measuredWidth
        val scaleFactor = 1 - Math.abs(offsetRate)
        if (scaleFactor > 0) {
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
            view.translationX = -maxTranslateOffsetX * offsetRate
        }
    }
}