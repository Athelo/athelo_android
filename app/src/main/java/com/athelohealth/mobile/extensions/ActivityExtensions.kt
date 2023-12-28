@file:Suppress("DEPRECATION")

package com.athelohealth.mobile.extensions

import android.app.Activity
import android.app.Dialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat


fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.getWindowInsetsController(window.decorView)?.isAppearanceLightStatusBars = true
}

fun Dialog.showSystemUI() {
    window?.let {
        WindowCompat.setDecorFitsSystemWindows(it, false)
        ViewCompat.getWindowInsetsController(it.decorView)?.isAppearanceLightStatusBars = true
    }
}