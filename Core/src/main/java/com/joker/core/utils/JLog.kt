package com.joker.core.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import android.util.Log.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * JLog
 * Log统一管理类
 * Debug版本输出LOG release 不输出LOG 不需要做其他处理
 *
 * @author joker
 * @date 2019/1/18.
 */
class JLog {

    private fun Log() {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {

        private val J_TAG = "JLog"
        private val LINE_SEPARATOR = "\n"
        private val MAX_LENGTH = 4000
        private val REGEX = "(.{$MAX_LENGTH})"

        private var tag = J_TAG
        private var debug = true
        private var title: String? = null

        //默认获取堆栈中的index
        val TRACE_INDEX_5 = 5
        //封装一层工具类，使用它获取堆栈中的index
        val TRACE_INDEX_6 = 6

        @JvmOverloads
        fun init(context: Context, tagName: String = J_TAG) {
            debug = context.applicationInfo != null && context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            tag = tagName
        }

        private fun getStackTraceElement(traceIndex: Int): StackTraceElement {
            val stackTrace = Thread.currentThread().stackTrace
            return stackTrace[traceIndex]
        }

        private fun getMethodNames(element: StackTraceElement) {
            val className = element.fileName
            val methodName = element.methodName
            val lineNumber = element.lineNumber

            //logcat中只要打印的内容中带有”(类名.java:行号)”就可以自动变为可点击的链接
            title = "JLog in ($className:$lineNumber) -> $methodName"
        }


        fun v(msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, VERBOSE)
            }
        }

        fun v(tag: String, msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, VERBOSE)
            }
        }

        fun d(msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, DEBUG)
            }
        }

        fun d(tag: String, msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, DEBUG)
            }
        }


        fun i(msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, INFO)
            }
        }

        fun i(tag: String, msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, INFO)
            }
        }


        fun w(msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, WARN)
            }
        }

        fun w(tag: String, msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, WARN)
            }
        }

        fun e(msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, ERROR)
            }
        }

        fun e(tag: String, msg: String, traceIndex: Int = TRACE_INDEX_5) {
            if (debug) {
                getMethodNames(getStackTraceElement(traceIndex))
                print(tag, msg, ERROR)
            }
        }

        private fun print(tag: String, msg: String?, type: Int) {
            var message: String

            if (msg == null) {
                message = "null"
            } else {
                message = try {
                    when {
                        msg.startsWith("{") -> {
                            val jsonObject = JSONObject(msg)
                            jsonObject.toString(4)
                        }
                        msg.startsWith("[") -> {
                            val jsonArray = JSONArray(msg)
                            jsonArray.toString(4)
                        }
                        else -> formatMessage(msg)
                    }
                } catch (e: Exception) {
                    formatMessage(msg)
                }

            }

            log(tag, "╔═════════════════════════════════════════════  $title  ══════════════════════════════════════════════", type)
            val lines = message.split(LINE_SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (line in lines) {
                log(tag, line, type)
            }
            log(tag, "╚═════════════════════════════════════════════════════════════════════════════════════════════════════", type)
        }

        private fun formatMessage(msg: String): String {
            return msg.replace(REGEX.toRegex(), "$1$LINE_SEPARATOR")
        }

        private fun log(tag: String, msg: String, type: Int) {
            when (type) {
                VERBOSE -> {
                    Log.v(tag, msg)
                    return
                }
                DEBUG -> {
                    Log.d(tag, msg)
                    return
                }
                INFO -> {
                    Log.i(tag, msg)
                    return
                }
                WARN -> {
                    Log.w(tag, msg)
                    return
                }
                ERROR -> {
                    Log.e(tag, msg)
                    return
                }
                else -> Log.v(tag, msg)
            }
        }
    }
}