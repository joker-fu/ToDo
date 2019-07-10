package com.joker.core.utils

import android.content.Context
import android.net.ConnectivityManager
import com.joker.core.BaseApp

/**
 * NetworkUtils
 *
 * @author  joker
 * @date    2018/4/24
 * @since
 */
object NetworkUtils {

    /**
     * 判断网络是否可用
     */
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = BaseApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * 检测wifi是否连接
     */
    fun isWifiConnected(): Boolean {
        val cm = BaseApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * 检测3G是否连接
     */
    fun is3gConnected(): Boolean {
        val cm = BaseApp.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE
    }
}
