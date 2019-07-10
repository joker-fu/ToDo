package com.joker.core.utils.activityresult

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * ActivityResultHelper
 *
 * @author joker
 * @date 2019/2/25
 */
class ActivityResultHelper {

    interface Callback {
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    companion object {
        private const val TAG = "_ActivityResultHelper_"

        fun with(activity: FragmentActivity): Fragment {
            var holderFragment = getHolderFragment(activity)

            if (holderFragment == null) {
                holderFragment = HolderFragment.getInstance()
                activity.supportFragmentManager.let {
                    it.beginTransaction()
                        .add(holderFragment, TAG)
                        .commitNowAllowingStateLoss()

                    it.executePendingTransactions()
                }
            }

            return holderFragment
        }

        fun with(fragment: Fragment): Fragment {
            val activity = fragment.requireActivity()
            return with(activity)
        }

        private fun getHolderFragment(activity: FragmentActivity): Fragment? {
            return activity.supportFragmentManager.findFragmentByTag(TAG)
        }
    }
}
