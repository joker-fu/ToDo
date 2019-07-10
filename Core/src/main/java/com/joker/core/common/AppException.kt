package com.joker.core.common


/**
 * AppException
 *
 * @author joker
 * @date 2019/1/25.
 */
class AppException(val code: Int?, override val message: String?) : Exception()