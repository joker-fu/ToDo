package com.joker.core.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import java.io.File


/**
 * NetworkUtils
 *
 * @author  joker
 * @date    2019/6/13
 * @since
 */
object PermissionUtils {

    /** 运行时权限 */
    fun runtime(
            activity: Activity,
            vararg permissions: String,
            onRationale: (context: Context, data: List<String>?) -> Boolean = { _, _ -> true },
            onGranted: (permissions: List<String>) -> Unit = { },
            onDenied: (isAlwaysDenied: Boolean, permissions: List<String>) -> Unit = { _, _ -> }
    ) {
        AndPermission.with(activity)
                .runtime()
                .permission(permissions)
                .rationale { context, data, executor ->
                    if (onRationale(context, data)) {
                        executor.execute()
                    } else {
                        executor.cancel()
                    }
                }
                .onGranted {
                    onGranted(it)
                }
                .onDenied {
                    // 这些权限被用户总是拒绝。
                    val isAlwaysDenied = AndPermission.hasAlwaysDeniedPermission(activity, it)
                    onDenied(isAlwaysDenied, it)
                }
                .start()

    }

    /** 运行时权限 */
    fun runtime(
            fragment: Fragment,
            vararg permissions: String,
            onRationale: (context: Context, data: List<String>?) -> Boolean = { _, _ -> true },
            onGranted: (permissions: List<String>) -> Unit = { },
            onDenied: (isAlwaysDenied: Boolean, permissions: List<String>) -> Unit = { _, _ -> }
    ) {
        AndPermission.with(fragment)
                .runtime()
                .permission(permissions)
                .rationale { context, data, executor ->
                    if (onRationale(context, data)) {
                        executor.execute()
                    } else {
                        executor.cancel()
                    }
                }
                .onGranted {
                    onGranted(it)
                }
                .onDenied {
                    // 这些权限被用户总是拒绝。
                    val isAlwaysDenied = AndPermission.hasAlwaysDeniedPermission(fragment, it)
                    onDenied(isAlwaysDenied, it)
                }
                .start()
    }

    /** 应用安装权限 */
    fun installApk(
            activity: Activity,
            onRationale: (context: Context, data: File?) -> Boolean = { _, _ -> true },
            onGranted: (file: File) -> Unit = { },
            onDenied: (file: File) -> Unit = { }
    ) {
        AndPermission.with(activity)
                .install()
                .rationale { context, data, executor ->
                    if (onRationale(context, data)) {
                        executor.execute()
                    } else {
                        executor.cancel()
                    }
                }
                .onGranted {
                    onGranted(it)
                }
                .onDenied {
                    onDenied(it)
                }
                .start()

    }

    /** 通知权限 */
    fun notification(
            activity: Activity,
            onRationale: (context: Context) -> Boolean = { true },
            onGranted: () -> Unit = { },
            onDenied: () -> Unit = { }
    ) {
        AndPermission.with(activity)
                .notification()
                .permission()
                .rationale { context, _, executor ->
                    if (onRationale(context)) {
                        executor.execute()
                    } else {
                        executor.cancel()
                    }
                }
                .onGranted {
                    onGranted()
                }
                .onDenied {
                    onDenied()
                }
                .start()
    }

    const val REQUEST_CODE_SETTING = 0x111

    /** 权限设置页 */
    fun toSetting(activity: Activity) {
        AndPermission.with(activity).runtime().setting().start(REQUEST_CODE_SETTING)
    }

    /** 权限设置页 */
    fun toSetting(fragment: Fragment) {
        AndPermission.with(fragment).runtime().setting().start(REQUEST_CODE_SETTING)
    }

    /** 转换权限字符串 **/
    fun transformPermissions(
            context: Context,
            permissions: List<String>,
            separator: CharSequence = "\n"
    ): String {
        val permissionNames = Permission.transformText(context, permissions)
        return permissionNames.joinToString(separator)
    }
    /*
    fun showSettingDialog(context: Context, permissions: List<String>) {
    val permissionNames = Permission.transformText(context, permissions)
    val message = context.getString(
        R.string.message_permission_always_failed,
        TextUtils.join("\n", permissionNames)
    )

    AlertDialog.Builder(context).setCancelable(true)
        .setTitle(R.string.title_dialog)
        .setMessage(message)
        .setPositiveButton(R.string.setting,
            DialogInterface.OnClickListener { dialog, which ->
                AndPermission.with(this).runtime().setting().start(REQUEST_CODE_SETTING)
            })
        .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which -> })
        .show()
    }*/
}