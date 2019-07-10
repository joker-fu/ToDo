package com.joker.core.ext

/**
 * other
 *
 * @author joker
 * @date 2019/6/25.
 */
inline fun countdown(times: Int, to: Int = 0, action: (Int) -> Unit) {
    val tempTo = if (to < 0) 0 else to
    for (index in times downTo tempTo) {
        action(index)
    }
}