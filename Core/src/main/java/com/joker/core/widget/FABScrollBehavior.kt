package com.joker.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

/**
 * FABScrollBehavior
 *
 * @author joker
 * @date 2019/1/28
 */
class FABScrollBehavior(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {

    private var hide = false

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        if (dependency is Snackbar.SnackbarLayout) {
            this.updateSnackbar(child, dependency)
        }
        return super.layoutDependsOn(parent, child, dependency)

    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        if (hide) return
        hideFab(child)
        hide = true
    }

    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        type: Int
    ) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type)
        if (!hide) return
        showFab(child)
        hide = false
    }

    private fun updateSnackbar(
        child: FloatingActionButton,
        snackbarLayout: Snackbar.SnackbarLayout
    ) {
        val layoutParams = snackbarLayout.layoutParams ?: throw RuntimeException(
            "null cannot be cast to non-null type android.support.design.widget.CoordinatorLayout.LayoutParams"
        )
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            layoutParams.anchorId = child.id
            layoutParams.anchorGravity = Gravity.TOP
            layoutParams.gravity = Gravity.TOP
            snackbarLayout.layoutParams = layoutParams
        }
    }


    private fun hideFab(fab: View) {
        fab.animate().cancel()
        fab.animate()
            .scaleX(0f)
            .scaleY(0f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    private fun showFab(fab: View) {
        fab.animate().cancel()
        fab.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }
}