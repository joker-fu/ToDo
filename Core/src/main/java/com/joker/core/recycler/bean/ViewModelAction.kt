package com.joker.core.recycler.bean

/**
 * ViewModelAction
 *
 * @author joker
 * @date 2019/1/23.
 */
class ViewModelAction private constructor(val state: Int, val message: String? = null) {

    companion object {
        //空闲
        const val ACTION_IDLE = 0
        //加载
        const val ACTION_LOADING = 1
        //成功
        const val ACTION_SUCCESS = 2
        //失败
        const val ACTION_FAIL = 3
        //完成
        const val ACTION_COMPLETE = 4
        //toast
        const val ACTION_TOAST = 5

        fun idle(): ViewModelAction {
            return ViewModelAction(ACTION_IDLE)
        }

        fun loading(message: String? = null): ViewModelAction {
            return ViewModelAction(ACTION_LOADING, message)
        }

        fun success(message: String? = null): ViewModelAction {
            return ViewModelAction(ACTION_SUCCESS, message)
        }

        fun fail(message: String? = null): ViewModelAction {
            return ViewModelAction(ACTION_FAIL, message)
        }

        fun complete(message: String? = null): ViewModelAction {
            return ViewModelAction(ACTION_COMPLETE, message)
        }

        fun toast(message: String? = null): ViewModelAction {
            return ViewModelAction(ACTION_TOAST, message)
        }
    }
}