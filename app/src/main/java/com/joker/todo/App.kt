package com.joker.todo

import com.joker.core.BaseApp
import com.joker.core.http.NetworkHelper
import com.joker.core.utils.SPUtils
import com.joker.todo.common.TODO_BASE_URL
import com.joker.todo.common.TODO_LOGIN
import com.joker.todo.common.TODO_LOGOUT
import com.joker.todo.common.TODO_REGISTER
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import okhttp3.Interceptor
import okhttp3.Response

/**
 * App
 *
 * @author joker
 * @date 2019/7/10
 */
class App : BaseApp() {

    override fun onCreate() {
        super.onCreate()
        NetworkHelper.build {
            baseUrl = TODO_BASE_URL
            interceptors.apply {
                add(Interceptor { chain ->
                    val req = chain.request()
                    val rep = chain.proceed(req)
                    val reqUrl = req.url().toString()
                    if (reqUrl.contains(TODO_LOGIN) || reqUrl.contains(TODO_REGISTER)) {
                        val cookies = rep.headers("Set-Cookie")
                        if (cookies.isNotEmpty()) {
                            SPUtils.put("login_cookie", cookies.toString())
                        }
                    }
                    if (reqUrl.contains(TODO_LOGOUT) && rep.isSuccessful) {
                        SPUtils.clear()
                    }
                    rep
                })
                add(Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    val cookie = SPUtils.get("login_cookie", "")
                    if (cookie != null) {
                        builder.header("Cookie", cookie.toString())
                    }
                    chain.proceed(builder.build())
                })
            }
        }.applyDefault()
    }

    companion object {
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.colorPrimary, R.color.white)//全局设置主题颜色
                layout.setEnableAutoLoadMore(true)
                //指定为经典Header，默认是 贝塞尔雷达Header
                ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setEnableLoadMoreWhenContentNotFull(false)
                layout.setEnableAutoLoadMore(true)
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}