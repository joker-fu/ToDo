package com.joker.core.ext

import com.joker.core.BuildConfig
import com.joker.core.common.AppException
import com.joker.core.http.HttpResponse
import com.joker.core.utils.JLog
import kotlinx.coroutines.CancellationException
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * ThrowableExt.kt
 *
 * @author joker
 * @date 2019/1/25.
 */
fun Throwable.wrap(): AppException {

    if (this is CancellationException) {
        //协程取消异常
        JLog.e(toString())
        return AppException(null, null)
    }

    return when (this) {
        is HttpException -> AppException(code(), if (BuildConfig.DEBUG) message else "网络异常")
        is ConnectException, is ConnectTimeoutException -> AppException(HttpResponse.HTTP_CONNECT_TIMEOUT, if (BuildConfig.DEBUG) message else "网络异常")
        is SocketTimeoutException -> AppException(HttpResponse.HTTP_RESPONSE_TIMEOUT, if (BuildConfig.DEBUG) message else "网络异常")
        is UnknownHostException -> AppException(HttpResponse.HTTP_UNKNOWN_HOST, if (BuildConfig.DEBUG) message else "网络异常")
        else -> AppException(HttpResponse.HTTP_SYSTEM_ERROR, if (BuildConfig.DEBUG) message else "系统异常")
    }
}
