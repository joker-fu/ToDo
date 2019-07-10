package com.joker.core.http

import com.joker.core.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.linjiang.pandora.Pandora
import java.net.Proxy
import java.util.concurrent.TimeUnit
import kotlin.jvm.internal.Intrinsics

/**
 * NetworkHelper
 *
 * @author joker
 * @date 2019/1/23.
 */
class NetworkHelper private constructor(builder: Builder) {

    private var baseUrl: String?
    private var timeout: Long
    private var cache: Cache?
    private var interceptors = mutableListOf<Interceptor>()
    private var okClient: OkHttpClient

    init {
        baseUrl = builder.baseUrl
        timeout = builder.timeout
        cache = builder.cache
        interceptors = builder.interceptors
        okClient = builder.okClient ?: createClient()
    }

    private fun createClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().also {
            it.level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

        val okBuilder = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(Pandora.get().interceptor)
            .proxy(Proxy.NO_PROXY)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .writeTimeout(timeout, TimeUnit.SECONDS)
            .callTimeout(timeout, TimeUnit.SECONDS)

        interceptors.forEach {
            okBuilder.addInterceptor(it)
        }

        return okBuilder.build()
    }

    private fun createRetrofit(): Retrofit {
        Intrinsics.checkNotNull(baseUrl, "base url is null~")
        return Retrofit.Builder()
            .baseUrl(baseUrl!!)
            .client(okClient)
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> create(clz: Class<T>): T {
        val key = clz.simpleName
        if (!cacheMap.containsKey(key)) {
            createRetrofit().create(clz).also {
                cacheMap[key] = it as Any
            }
        }
        return cacheMap[key] as T
    }

    fun applyDefault() {
        default = this
    }

    class Builder {

        var baseUrl: String? = null
        var timeout: Long = 30L
        var interceptors = mutableListOf<Interceptor>()

        //Cache(File(context.cacheDir, "HttpCache"), 1024 * 1024 * 10)
        val cache: Cache? = null
        var okClient: OkHttpClient? = null

        fun build() = NetworkHelper(this)
    }

    companion object {

        private val cacheMap: HashMap<String, Any> = hashMapOf()

        private var default: NetworkHelper? = null

        inline fun build(block: Builder.() -> Unit = {}) = Builder().apply(block).build()

        fun getDefault(): NetworkHelper {
            Intrinsics.checkNotNull(default, "default NetworkHelper is null~")
            return default!!
        }
    }
}

inline fun <reified T> NetworkHelper.create(): T {
    return this.create(T::class.java)
}
