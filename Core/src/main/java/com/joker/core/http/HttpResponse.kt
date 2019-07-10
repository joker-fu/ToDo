package com.joker.core.http

import com.google.gson.annotations.SerializedName
import kotlin.coroutines.suspendCoroutine

/**
 * HttpResponse
 *
 * @author  joker
 * @date    2019/1/24
 */
class HttpResponse<T> {

    @SerializedName("code")
    var code: Int = 0

    @SerializedName("msg")
    var message: String? = ""

    var `data`: T? = null

    companion object {

        /** 没有网络 **/
        const val HTTP_NETWORK_ERROR = -0x11
        /** 网络连接超时 **/
        const val HTTP_CONNECT_TIMEOUT = -0x22
        /** 网络响应超时 **/
        const val HTTP_RESPONSE_TIMEOUT = -0x33
        /** 响应错误 **/
        const val HTTP_RESPONSE_ERROR = -0x44
        /** 主机IP无法确定 **/
        const val HTTP_UNKNOWN_HOST = -0x55
        /** 未知错误 **/
        const val HTTP_SYSTEM_ERROR = -0xff

        /** 请求成功 **/
        const val HTTP_SUCCESS = 0 //200
        /** 服务器错误 **/
        const val HTTP_SERVER_ERROR = 400

    }

    fun isSuccessful(): Boolean {
        return code == HTTP_SUCCESS
    }
}


suspend fun <T> HttpResponse<T>.get(): T? {
    return suspendCoroutine {
        if (isSuccessful()) {
            it.resumeWith(Result.success(data))
        } else {
            it.resumeWith(Result.failure(ErrorResponse(code, message)))
        }
    }
}
