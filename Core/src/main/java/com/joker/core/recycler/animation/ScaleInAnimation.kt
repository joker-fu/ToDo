package com.joker.core.recycler.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * ScaleInAnimation
 *
 * @author joker
 * @date 2019/1/20.
 */
class ScaleInAnimation @JvmOverloads constructor(private val from: Float = .5f) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", from, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", from, 1f)
        return arrayOf(scaleX, scaleY)
    }

}