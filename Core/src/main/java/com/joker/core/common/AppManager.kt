package com.joker.core.common

import android.app.Activity
import android.content.Intent
import com.joker.core.utils.JLog
import java.util.*

/**
 * AppManager
 *
 * @author joker
 * @date 2019/1/18.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class AppManager private constructor() {

    /** Activity入栈 **/
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    /** Activity出栈 **/
    fun removeActivity(activity: Activity) {
        if (activity.isFinishing) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /** 获取栈顶（当前）Activity **/
    fun getTopActivity(): Activity {
        return activityStack.lastElement()
    }

    /** 移除栈中所有 Activity **/
    fun removeAllActivity() {
        removeOthersActivity(null)
        activityStack.clear()
    }

    /** 移除栈中其他 Activity **/
    fun removeOthersActivity(clz: Class<*>?) {
        activityStack.forEach {
            if (it.javaClass != clz) {
                removeActivity(it)
            }
        }
    }

    /** 返回桌面 **/
    fun back2Desktop() {
        val home = Intent(Intent.ACTION_MAIN)
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        home.addCategory(Intent.CATEGORY_HOME)
        getTopActivity().startActivity(home)
    }

    /** 退出应用程序 **/
    fun exitApp() {
        try {
            removeAllActivity()
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            JLog.e(e.toString())
        }
    }

    companion object {
        val manager = Companion.Holder.instance
        private val activityStack: Stack<Activity> = Stack()

        private object Holder {
            val instance = AppManager()
        }
    }
}