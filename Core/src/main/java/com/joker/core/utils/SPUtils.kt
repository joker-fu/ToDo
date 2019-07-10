package com.joker.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joker.core.BaseApp


/**
 * SPUtils
 *
 * @author  joker
 * @date    2019/1/24
 */
@Suppress("HasPlatformType")
@SuppressLint("ApplySharedPref")
class SPUtils private constructor(private val sp: SharedPreferences) {

    fun <T> put(key: String, value: T, isCommit: Boolean = false) = with(sp.edit()) {
        try {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                else -> {
                    val json = Gson().toJson(value)
                    putString(key, json)
                }
            }.let {
                if (isCommit) it.commit() else it.apply()
            }
        } catch (e: Exception) {
            JLog.e(e.toString())
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, def: T?): T? = with(sp) {
        val res: Any? = when (def) {
            is Long -> getLong(key, (def as? Long) ?: 0)
            is String -> getString(key, (def as? String) ?: "")
            is Int -> getInt(key, (def as? Int) ?: 0)
            is Boolean -> getBoolean(key, (def as? Boolean) ?: false)
            is Float -> getFloat(key, (def as? Float) ?: 0f)
            else -> null
        }
        return res as? T
    }

    fun <T> getObj(key: String, clz: Class<T>): T? {
        return try {
            val json = get(key, "")
            Gson().fromJson(json, clz)
        } catch (e: Exception) {
            JLog.e(e.toString())
            null
        }
    }

    fun <T> getList(key: String): T? = with(sp) {
        return try {
            val json = getString(key, "")
            val type = object : TypeToken<MutableList<T>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            JLog.e(e.toString())
            null
        }
    }

    fun getAll() = sp.all

    fun contains(key: String): Boolean = sp.contains(key)

    fun remove(key: String, isCommit: Boolean = false) = sp.edit().remove(key).let {
        if (isCommit) it.commit() else it.apply()
    }

    fun clear(isCommit: Boolean = false) = sp.edit().clear().let {
        if (isCommit) it.commit() else it.apply()
    }

    companion object {

        const val DEFAULT_SP_NAME = "app_def_sp"

        private val spUtilMap: HashMap<String, SPUtils> = hashMapOf()

        @Suppress("UNCHECKED_CAST")
        @JvmOverloads
        @JvmStatic
        fun getInstance(spName: String = DEFAULT_SP_NAME, mode: Int = Context.MODE_PRIVATE): SPUtils {
            var spUtil = spUtilMap[spName]
            if (spUtil == null) {
                val sp = BaseApp.instance.getSharedPreferences(spName, mode)
                spUtil = SPUtils(sp)
                spUtilMap[spName] = spUtil
            }
            return spUtil
        }

        fun <T> put(
                key: String,
                value: T,
                isCommit: Boolean = false,
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ) = getInstance(spName, mode).put(key, value, isCommit)

        inline fun <reified T> get(
                key: String,
                default: T? = null,
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ): T? = if (Collection::class.java.isAssignableFrom(T::class.java)) {
            getInstance(spName, mode).getList(key)
        } else {
            val spUtils = getInstance(spName, mode)
            spUtils.get(key, default) ?: spUtils.getObj(key, T::class.java)
        }

        fun getAll(
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ) = getInstance(spName, mode).getAll()

        fun contains(
                key: String,
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ): Boolean = getInstance(spName, mode).contains(key)

        fun remove(
                key: String,
                isCommit: Boolean = false,
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ) = getInstance(spName, mode).remove(key, isCommit)

        fun clear(
                isCommit: Boolean = false,
                spName: String = DEFAULT_SP_NAME,
                mode: Int = Context.MODE_PRIVATE
        ) = getInstance(spName, mode).clear(isCommit)
    }
}