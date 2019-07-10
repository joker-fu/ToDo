package com.joker.todo.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.joker.core.common.AppManager
import com.joker.core.ui.base.BaseActivity
import com.joker.todo.R
import com.joker.todo.ui.main.center.CenterFragment
import com.joker.todo.ui.main.inventory.InventoriesFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 *
 * @author  joker
 * @date    2019/7/10
 */
class MainActivity : BaseActivity<Nothing>() {
    override val layout: Any
        get() = R.layout.activity_main

    override fun onBindView(savedInstanceState: Bundle?) {
        viewPager.isCanScroll = false
        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            val fragments = listOf(InventoriesFragment(), CenterFragment())

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return fragments[position].toString()
            }

            override fun getCount(): Int {
                return fragments.size
            }

        }
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) = Unit

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

            override fun onPageSelected(position: Int) {
                bottomNavigation.menu.getItem(position).isChecked = true
            }

        })
        bottomNavigation.setOnNavigationItemSelectedListener {
            val item = when (it.itemId) {
                R.id.menu_list -> 0
                R.id.menu_center -> 1
                else -> 0
            }
            viewPager.setCurrentItem(item, true)
            true
        }
    }

    override fun onBackPressed() {
        AppManager.manager.back2Desktop()
    }

}
