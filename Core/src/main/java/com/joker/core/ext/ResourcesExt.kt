package com.joker.core.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * ResourcesExt
 *
 * @author joker
 * @date 2019/1/18.
 */

fun View.string(@StringRes resId: Int, vararg formatArgs: Any?): String = resources.getString(resId, *formatArgs)

fun View.stringArray(@ArrayRes resId: Int) = resources.getStringArray(resId)

fun View.color(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)

fun View.drawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(context, resId)!!


fun Fragment.string(@StringRes resId: Int, vararg formatArgs:  Any?): String = resources.getString(resId, *formatArgs)

fun Fragment.stringArray(@ArrayRes resId: Int) = resources.getStringArray(resId)

fun Fragment.color(@ColorRes resId: Int): Int = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(requireContext(), resId)!!


fun Context.string(@StringRes resId: Int, vararg formatArgs:  Any?): String = resources.getString(resId, *formatArgs)

fun Context.stringArray(@ArrayRes resId: Int) = resources.getStringArray(resId)

fun Context.color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.drawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(this, resId)!!



