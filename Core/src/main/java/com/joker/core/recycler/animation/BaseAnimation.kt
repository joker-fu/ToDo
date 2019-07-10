package com.joker.core.recycler.animation

import android.animation.Animator
import android.view.View

/**
 * BaseAnimation
 *
 * @author joker
 * @date 2019/1/20.
 */
interface BaseAnimation {

    fun getAnimators(view: View): Array<Animator>

}