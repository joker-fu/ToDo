package com.joker.core.http


/**
 * ErrorResponse
 *
 * @author joker
 * @date 2019/1/25.
 */
class ErrorResponse(code: Int?, message: String? = null) : Exception(message)
