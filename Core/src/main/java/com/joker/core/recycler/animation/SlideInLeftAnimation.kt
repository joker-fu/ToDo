package com.joker.core.recycler.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View


/**
 * SlideInLeftAnimation
 *
 * @author joker
 * @date 2019/1/20.
 */
class SlideInLeftAnimation : BaseAnimation {

    override fun getAnimators(view: View): Array<Animator> {
        return arrayOf(ObjectAnimator.ofFloat(view, "translationX", -view.rootView.width.toFloat(), 0f))
    }

}