package com.joker.core.utils.activityresult

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import java.util.*


/**
 * HolderFragment
 *
 * @author  joker
 * @date    2019/2/25
 */
internal class HolderFragment : Fragment() {

    private val callbacks = SparseArray<ActivityResultHelper.Callback>()
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startActivityForResult(
        intent: Intent,
        options: Bundle?,
        callback: ActivityResultHelper.Callback
    ) {
        val requestCode = createRequestCode()
        callbacks.put(requestCode, callback)
        startActivityForResult(intent, requestCode, options)
    }

    /**
     * 随机生成唯一的requestCode，最多尝试100次
     */
    private fun createRequestCode(): Int {
        var requestCode: Int
        var tryCount = 0
        do {
            requestCode = random.nextInt(0x0000FFFF)
            tryCount++
        } while (callbacks.indexOfKey(requestCode) >= 0 && tryCount < 100)
        return requestCode
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbacks[requestCode].let { callback ->
            callback.onActivityResult(requestCode, resultCode, data)
            callbacks.remove(requestCode)
        }
    }

    companion object {
        fun getInstance(): HolderFragment {
            return HolderFragment()
        }
    }
}