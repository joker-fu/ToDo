package com.joker.core

import android.app.Application


/**
 * BaseApp
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("LeakingThis")
open class BaseApp : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        setRxJavaErrorHandler()
        /*
        UMConfigure.init(this, "5cfbe1c0570df336e7000448", null, UMConfigure.DEVICE_TYPE_PHONE, null)
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        PlatformConfig.setWeixin("","")
        PlatformConfig.setQQZone("","")
        */
    }

    //设置RxJavaPlugin的ErrorHandler
    private fun setRxJavaErrorHandler() {
       /* RxJavaPlugins.setErrorHandler {
            it.printStackTrace()
        }*/
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}