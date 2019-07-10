package com.joker.core.widget

import android.content.res.Resources
import android.graphics.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import java.security.MessageDigest
import kotlin.math.min

/**
 * FABScrollBehavior
 *
 * @author joker
 * @date 2019/6/20
 */
class CustomCircleCrop(borderWidth: Int, borderColor: Int) : BitmapTransformation() {

    private val mBorderWidth: Float = Resources.getSystem().displayMetrics.density * borderWidth
    private val mBorderPaint: Paint?

    init {
        mBorderPaint = Paint()
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return circleCrop(pool, toTransform, outWidth, outHeight)
    }


    private fun circleCrop(pool: BitmapPool, source: Bitmap?, destWidth: Int, destHeight: Int): Bitmap? {
        val destMinEdge = min(destWidth, destHeight)
        if (source == null) return null

        val size = (destMinEdge - mBorderWidth / 2).toInt()
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val squared = Bitmap.createBitmap(source, x, y, size, size)
        var result: Bitmap? = pool.get(size, size, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(squared, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        if (mBorderPaint != null) {
            val borderRadius = r - mBorderWidth / 2
            canvas.drawCircle(r, r, borderRadius, mBorderPaint)
        }
        return result
    }


    override fun equals(o: Any?): Boolean {
        return o is CircleCrop
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private val VERSION = 1
        private val ID = "com.bumptech.glide.load.resource.bitmap.CircleCrop.$VERSION"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
    }
}
