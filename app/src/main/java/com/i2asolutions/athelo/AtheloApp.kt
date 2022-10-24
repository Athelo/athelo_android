package com.i2asolutions.athelo

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import com.i2asolutions.sff.utils.internetStatus.provider.ConnectivityProvider
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AtheloApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupNetworkConnection()
    }

    private fun setupNetworkConnection() {
        if (isMainProcess()) {
            ConnectivityProvider.createProvider(this).subscribe()
        }
    }

    private fun isMainProcess(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageName == getProcessName()
        } else packageName == getProcessNameLegacy()
    }

    // you can use this method to get current process name, you will get
    private fun getProcessNameLegacy(): String? {
        val mypid = Process.myPid()
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        for (info in infos) {
            if (info.pid == mypid) {
                return info.processName
            }
        }
        // may never return null
        return null
    }
}