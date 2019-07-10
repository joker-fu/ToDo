package com.joker.core.ext

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.joker.core.common.AppManager
import com.joker.core.ui.base.BaseFragment
import com.joker.core.utils.Toaster
import com.joker.core.widget.LoadingDialog
import org.jetbrains.anko.*
import org.jetbrains.anko.internals.AnkoInternals
import java.io.Serializable

/**
 * FragmentExt
 *
 * @author joker
 * @date 2019/1/18.
 */
fun Fragment.hasIntentParams(name: String): Boolean {
    return arguments != null && arguments!!.containsKey(name)
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T : Any?> Fragment.getIntentParams(name: String, def: T? = null): T? {

    arguments?.apply {
        if (containsKey(name)) {
            val res = when {
                Int::class.java.isAssignableFrom(T::class.java) -> getInt(name)
                Long::class.java.isAssignableFrom(T::class.java) -> getLong(name)
                CharSequence::class.java.isAssignableFrom(T::class.java) -> getCharSequence(name)
                String::class.java.isAssignableFrom(T::class.java) -> getString(name)
                Float::class.java.isAssignableFrom(T::class.java) -> getFloat(name)
                Double::class.java.isAssignableFrom(T::class.java) -> getDouble(name)
                Char::class.java.isAssignableFrom(T::class.java) -> getChar(name)
                Short::class.java.isAssignableFrom(T::class.java) -> getShort(name)
                Boolean::class.java.isAssignableFrom(T::class.java) -> getBoolean(name)
                Serializable::class.java.isAssignableFrom(T::class.java) -> getSerializable(name)
                Bundle::class.java.isAssignableFrom(T::class.java) -> getBundle(name)
                Parcelable::class.java.isAssignableFrom(T::class.java) -> getParcelable(name)
                IntArray::class.java.isAssignableFrom(T::class.java) -> getIntArray(name)
                LongArray::class.java.isAssignableFrom(T::class.java) -> getLongArray(name)
                FloatArray::class.java.isAssignableFrom(T::class.java) -> getFloatArray(name)
                DoubleArray::class.java.isAssignableFrom(T::class.java) -> getDoubleArray(name)
                CharArray::class.java.isAssignableFrom(T::class.java) -> getCharArray(name)
                ShortArray::class.java.isAssignableFrom(T::class.java) -> getShortArray(name)
                BooleanArray::class.java.isAssignableFrom(T::class.java) -> getByteArray(name)
                else -> throw IllegalArgumentException("$name  not support")
            }
            return res as? T ?: def
        }
    }
    return def

}

fun Fragment.showSoftKeyBoard(view: View?) {
    activity?.showSoftKeyBoard(view)
}

fun Fragment.hideSoftKeyBoard() {
    activity?.hideSoftKeyBoard()
}

fun BaseFragment<*>.toast(@StringRes resId: Int) {
    val activity = activity ?: AppManager.manager.getTopActivity()
    Toaster.showToast(activity, resId)
}

fun BaseFragment<*>.toast(charSequence: CharSequence?) {
    if (!charSequence.isNullOrEmpty()) {
        val activity = activity ?: AppManager.manager.getTopActivity()
        Toaster.showToast(activity, charSequence)
    }
}

fun BaseFragment<*>.showLoading(resId: Int? = null) {
    loadingDialog = LoadingDialog.Builder(requireActivity()).setMessage(resId).create()
    loadingDialog?.show(this)
}

fun BaseFragment<*>.showLoading(str: String?) {
    loadingDialog = LoadingDialog.Builder(requireActivity()).setMessage(str).create()
    loadingDialog?.show(this)
}

fun BaseFragment<*>.hideLoading() {
    loadingDialog?.dismiss()
}

fun Fragment.getPackageInfo(): PackageInfo? {
    var info: PackageInfo? = null
    activity?.apply {
        info = packageManager.getPackageInfo(
                packageName, PackageManager.GET_CONFIGURATIONS
        )
    }
    return info
}

// Anko AndroidX Fragment 相关扩展

inline fun Fragment.dip(value: Int): Int = requireContext().dip(value)
inline fun Fragment.dip(value: Float): Int = requireContext().dip(value)
inline fun Fragment.sp(value: Int): Int = requireContext().sp(value)
inline fun Fragment.sp(value: Float): Int = requireContext().sp(value)
inline fun Fragment.px2dip(px: Int): Float = requireContext().px2dip(px)
inline fun Fragment.px2sp(px: Int): Float = requireContext().px2sp(px)
inline fun Fragment.dimen(@DimenRes resource: Int): Int = requireContext().dimen(resource)

inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, Any?>) =
        AnkoInternals.internalStartActivity(requireActivity(), T::class.java, params)

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, vararg params: Pair<String, Any?>) =
        startActivityForResult(AnkoInternals.createIntent(requireActivity(), T::class.java, params), requestCode)

inline fun <reified T : Service> Fragment.startService(vararg params: Pair<String, Any?>) =
        AnkoInternals.internalStartService(requireActivity(), T::class.java, params)

inline fun <reified T : Service> Fragment.stopService(vararg params: Pair<String, Any?>) =
        AnkoInternals.internalStopService(requireActivity(), T::class.java, params)

inline fun <reified T : Any> Fragment.intentFor(vararg params: Pair<String, Any?>): Intent =
        AnkoInternals.createIntent(requireActivity(), T::class.java, params)

inline fun Fragment.browse(url: String, newTask: Boolean = false) = requireActivity().browse(url, newTask)

inline fun Fragment.share(text: String, subject: String = "") = requireActivity().share(text, subject)

inline fun Fragment.email(email: String, subject: String = "", text: String = "") =
        requireActivity().email(email, subject, text)

inline fun Fragment.makeCall(number: String): Boolean = requireActivity().makeCall(number)

inline fun Fragment.sendSMS(number: String, text: String = ""): Boolean = requireActivity().sendSMS(number, text)
