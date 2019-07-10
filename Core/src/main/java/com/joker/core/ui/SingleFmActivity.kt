package com.joker.core.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.joker.core.R
import com.joker.core.ui.base.BaseActivity


/**
 * SingleFmActivity
 *
 * @author  joker
 * @date    2019/1/18
 */
class SingleFmActivity : BaseActivity<Nothing>() {

    override val layout: Any
        get() = R.layout.activity_single_fragment_core

    private lateinit var fragment: Fragment

    override fun onBindView(savedInstanceState: Bundle?) {
        if (intent.hasExtra(PARAMS_NAME)) {
            val nameExtra = intent.getStringExtra(PARAMS_NAME)
            val bundle = intent.getBundleExtra(PARAMS_BUNDLE)
            try {
                fragment = Fragment.instantiate(this, nameExtra, bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            throw IllegalArgumentException("don not have the class name of the fragment to instantiate.")
        }
        if (!::fragment.isInitialized) {
            throw IllegalArgumentException("the fragment is not init.")
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contentFrameLayout, fragment)
        transaction.commit()
    }

    companion object {

        const val PARAMS_NAME = "PARAMS_NAME"
        const val PARAMS_BUNDLE = "PARAMS_BUNDLE"

        inline fun <reified T : Fragment> start(context: Context) {
            start<T>(context, null)
        }

        inline fun <reified T : Fragment> start(context: Context, args: Bundle?) {
            val intent = Intent(context, SingleFmActivity::class.java)
            intent.putExtra(PARAMS_NAME, T::class.java.name)
            intent.putExtra(PARAMS_BUNDLE, args)
            context.startActivity(intent)
        }

        inline fun <reified T : Fragment> start4Result(requestCode: Int, activity: Activity) {
            start4Result<T>(requestCode, activity, null)
        }

        inline fun <reified T : Fragment> start4Result(requestCode: Int, activity: Activity, args: Bundle?) {
            val intent = Intent(activity, SingleFmActivity::class.java)
            intent.putExtra(PARAMS_NAME, T::class.java.name)
            intent.putExtra(PARAMS_BUNDLE, args)
            activity.startActivityForResult(intent, requestCode)
        }

        inline fun <reified T : Fragment> start4Result(requestCode: Int, fragment: Fragment) {
            start4Result<T>(requestCode, fragment, null)
        }

        inline fun <reified T : Fragment> start4Result(requestCode: Int, fragment: Fragment, args: Bundle?) {
            val intent = Intent(fragment.activity, SingleFmActivity::class.java)
            intent.putExtra(PARAMS_NAME, T::class.java.name)
            intent.putExtra(PARAMS_BUNDLE, args)
            fragment.startActivityForResult(intent, requestCode)
        }

    }

}