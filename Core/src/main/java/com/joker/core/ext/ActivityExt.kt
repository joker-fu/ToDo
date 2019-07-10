package com.joker.core.ext

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import com.joker.core.ui.base.BaseActivity
import com.joker.core.utils.Toaster
import com.joker.core.widget.LoadingDialog
import java.io.Serializable

/**
 * ActivityExt
 *
 * @author joker
 * @date 2019/1/18.
 */
fun Activity.hasIntentParams(name: String): Boolean {
    return intent.hasExtra(name)
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T : Any?> Activity.getIntentParams(name: String, def: T? = null): T? {

    if (intent.hasExtra(name)) {
        val res = when {
            Int::class.java.isAssignableFrom(T::class.java) ->
                intent.getIntExtra(name, (def as? Int) ?: 0)
            Long::class.java.isAssignableFrom(T::class.java) ->
                intent.getLongExtra(name, (def as? Long) ?: 0L)
            Float::class.java.isAssignableFrom(T::class.java) ->
                intent.getFloatExtra(name, (def as? Float) ?: 0F)
            Double::class.java.isAssignableFrom(T::class.java) ->
                intent.getDoubleExtra(name, (def as? Double) ?: 0.0)
            Char::class.java.isAssignableFrom(T::class.java) ->
                intent.getCharExtra(name, (def as? Char) ?: 0.toChar())
            Short::class.java.isAssignableFrom(T::class.java) ->
                intent.getShortExtra(name, (def as? Short) ?: 0.toShort())
            Boolean::class.java.isAssignableFrom(T::class.java) ->
                intent.getBooleanExtra(name, (def as? Boolean) ?: false)
            CharSequence::class.java.isAssignableFrom(T::class.java) -> intent.getCharSequenceExtra(name)
            String::class.java.isAssignableFrom(T::class.java) -> intent.getStringExtra(name)
            Serializable::class.java.isAssignableFrom(T::class.java) -> intent.getSerializableExtra(name)
            Bundle::class.java.isAssignableFrom(T::class.java) -> intent.getBundleExtra(name)
            Parcelable::class.java.isAssignableFrom(T::class.java) -> intent.getParcelableExtra(name)
            IntArray::class.java.isAssignableFrom(T::class.java) -> intent.getIntArrayExtra(name)
            LongArray::class.java.isAssignableFrom(T::class.java) -> intent.getLongArrayExtra(name)
            FloatArray::class.java.isAssignableFrom(T::class.java) -> intent.getFloatArrayExtra(name)
            DoubleArray::class.java.isAssignableFrom(T::class.java) -> intent.getDoubleArrayExtra(name)
            CharArray::class.java.isAssignableFrom(T::class.java) -> intent.getCharArrayExtra(name)
            ShortArray::class.java.isAssignableFrom(T::class.java) -> intent.getShortArrayExtra(name)
            BooleanArray::class.java.isAssignableFrom(T::class.java) -> intent.getByteArrayExtra(name)
            else -> throw IllegalArgumentException("$name-> type:${T::class.java.simpleName} not support")
        }
        return res as? T ?: def
    }
    return def
}

fun Activity.showSoftKeyBoard(view: View?) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    if (view != null && imm != null) {
        view.requestFocus()
        imm.showSoftInput(view, 0)
    }
}

fun Activity.hideSoftKeyBoard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    val localView = currentFocus
    if (localView != null && imm != null) {
        imm.hideSoftInputFromWindow(localView.windowToken, 0)
    }
}

fun BaseActivity<*>.toast(@StringRes resId: Int) {
    Toaster.showToast(this, resId)
}

fun BaseActivity<*>.toast(charSequence: CharSequence?) {
    if (!charSequence.isNullOrEmpty()) {
        Toaster.showToast(this, charSequence)
    }
}

fun BaseActivity<*>.showLoading(resId: Int? = null) {
    loadingDialog = LoadingDialog.Builder(this).setMessage(resId).create()
    loadingDialog?.show(this)
}

fun BaseActivity<*>.showLoading(str: String?) {
    loadingDialog = LoadingDialog.Builder(this).setMessage(str).create()
    loadingDialog?.show(this)
}

fun BaseActivity<*>.hideLoading() {
    loadingDialog?.dismiss()
}

fun Activity.getPackageInfo(): PackageInfo? {
    return packageManager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS)
}
