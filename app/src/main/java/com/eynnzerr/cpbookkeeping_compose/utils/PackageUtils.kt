package com.eynnzerr.cpbookkeeping_compose.utils

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.eynnzerr.cpbookkeeping_compose.base.CPApplication

fun getVersionCode(): Long {
    val manager = CPApplication.context.packageManager
    var code = 1L
    try {
        val info = manager.getPackageInfo(CPApplication.context.packageName, 0)
        code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            info.versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return code
}