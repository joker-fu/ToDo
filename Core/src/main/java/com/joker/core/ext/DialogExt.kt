package com.joker.core.ext

import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lxj.xpopup.XPopup

/**
 * DialogExt
 *
 * @author  joker
 * @date    2019/2/21
 */
fun Fragment.alertCommonDialog(
        @StringRes title: Int,
        @StringRes content: Int
        , onYes: () -> Unit,
        onNo: () -> Unit = {}
) {
    activity?.let {
        val titleStr = it.getString(title)
        val contentStr = it.getString(content)

        XPopup.Builder(it)
                .asConfirm(titleStr, contentStr, onYes, onNo)
                .show()
    }
}

fun Fragment.alertCommonDialog(
        title: String,
        content: String,
        onYes: () -> Unit,
        onNo: () -> Unit = {}
) {
    activity?.let {
        XPopup.Builder(it)
                .asConfirm(title, content, onYes, onNo)
                .show()
    }
}

fun AppCompatActivity.alertListDialog(
        @StringRes title: Int,
        items: List<String>,
        onItemClick: (dialog: DialogInterface, which: Int) -> Unit
) {
    AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(items.toTypedArray(), onItemClick)
            .show()
}