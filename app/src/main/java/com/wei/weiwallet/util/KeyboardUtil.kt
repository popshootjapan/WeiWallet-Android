package com.wei.weiwallet.util

import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context

object KeyboardUtil {
    fun copy(context: Context, label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(label, text)
    }

    fun paste(context: Context): String {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return if (!clipboard.hasPrimaryClip()) ""
        else if (!clipboard.primaryClipDescription.hasMimeType(MIMETYPE_TEXT_PLAIN)) ""
        else clipboard.primaryClip.getItemAt(0).text.toString()
    }
}
