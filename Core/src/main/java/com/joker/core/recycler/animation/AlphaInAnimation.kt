package com.joker.core.recycler.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * AlphaInAnimation
 *
 * @author joker
 * @date 2019/1/20.
 */
class AlphaInAnimation @JvmOverloads constructor(private val from: Float = 0f) : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "alpha", from, 1f))
    }

}