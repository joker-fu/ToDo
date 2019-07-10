package com.joker.core.recycler.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View

/**
 * SlideInBottomAnimation
 *
 * @author joker
 * @date 2019/1/20.
 */
class SlideInBottomAnimation : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f))
    }

}