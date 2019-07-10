package com.joker.core.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target


/**
 * ImageViewExt
 *
 * @author  joker
 * @date    2019/2/26
 */

fun ImageView.loadImage(path: Any?, isCircle: Boolean = false, roundRadius: Int = 0) =
    load(this, path, isCircle = isCircle, roundRadius = roundRadius)

fun ImageView.loadImage(fragment: Fragment, path: Any?, isCircle: Boolean = false, roundRadius: Int = 0) =
    load(fragment, path, isCircle = isCircle, roundRadius = roundRadius)

fun ImageView.loadImage(activity: Activity, path: Any?, isCircle: Boolean = false, roundRadius: Int = 0) =
    load(activity, path, isCircle = isCircle, roundRadius = roundRadius)

fun ImageView.loadImage(activity: FragmentActivity, path: Any?, isCircle: Boolean = false, roundRadius: Int = 0) =
    load(activity, path, isCircle = isCircle, roundRadius = roundRadius)

fun ImageView.loadImage(context: Context, path: Any?, isCircle: Boolean = false, roundRadius: Int = 0) =
    load(context, path, isCircle = isCircle, roundRadius = roundRadius)

/**
 * Glide加载图片
 * @param context Glide.with() 支持的上下文
 * @param url 可以是网络，可以是File，可以是资源id等等Glide支持的类型
 * @param placeholder 默认占位图
 * @param error 失败占位图
 * @param isCircle 是否是圆形，默认false，【注意：isCircle和roundRadius两个只能有一个生效】
 * @param isCenterCrop 是否设置scaleType为CenterCrop，你也可以在布局文件中设置
 * @param roundRadius 圆角角度，默认为0，不带圆角 【注意：isCircle和roundRadius两个只能有一个生效】
 * @param isCrossFade 是否有过渡动画，默认没有过渡动画
 * @param isForceOriginalSize 是否强制使用原图，默认false
 * @param options RequestOptions 【注意设置了此参数，上面所有参数失效】
 */
fun ImageView.load(
    context: Any?,
    url: Any?,
    placeholder: Int = 0,
    error: Int = 0,
    isCircle: Boolean = false,
    isCenterCrop: Boolean = false,
    roundRadius: Int = 0,
    isCrossFade: Boolean = false,
    isForceOriginalSize: Boolean = false,
    options: RequestOptions? = null
) {
    val realOptions =
        options ?: RequestOptions()
            .placeholder(placeholder)
            .error(error)
            .apply {
                /*if (isCenterCrop && scaleType != ImageView.ScaleType.CENTER_CROP) {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }*/
                if (isCenterCrop || scaleType == ImageView.ScaleType.CENTER_CROP) {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    transforms(CenterCrop())
                }
                if (isCircle) {
                    if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                        transforms(CenterCrop(), CircleCrop())
                    } else {
                        transform(CircleCrop())
                    }
                } else if (roundRadius != 0) {
                    if (scaleType == ImageView.ScaleType.CENTER_CROP) {
                        transforms(CenterCrop(), RoundedCorners(roundRadius))
                    } else {
                        transform(RoundedCorners(roundRadius))
                    }
                }
                if (isForceOriginalSize) {
                    override(Target.SIZE_ORIGINAL)
                }
            }

    val glide = when (context) {
        is View -> Glide.with(context)
        is Fragment -> Glide.with(context)
        is FragmentActivity -> Glide.with(context)
        is Activity -> Glide.with(context)
        is Context -> Glide.with(context)
        else -> throw IllegalArgumentException("the context must be Glide supported")
    }

    glide.load(url)
        .apply(realOptions)
        .apply {
            if (isCrossFade) transition(withCrossFade())
        }
        .into(this)
}