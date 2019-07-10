package com.joker.core.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * JsonUtils
 *
 * @author  joker
 * @date    2019/1/24
 */
object JsonUtils {

    fun toJson(src: Any?): String? {
        if (src == null) return null
        return Gson().toJson(src)
    }

    inline fun <reified T> fromJson(src: String?): T? {
        if (src == null) return null
        return Gson().fromJson(src, T::class.java)
    }

    inline fun <reified T> fromJsonList(src: String?): MutableList<T>? {
        if (src == null) return null
        val type = object : TypeToken<MutableList<T>>() {}.type
        return Gson().fromJson(src, type)
    }

}