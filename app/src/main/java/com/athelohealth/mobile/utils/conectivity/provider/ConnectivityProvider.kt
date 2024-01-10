package com.athelohealth.mobile.utils.conectivity.provider

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.athelohealth.mobile.utils.conectivity.NetworkState

interface ConnectivityProvider {
    val networkState: NetworkState
    fun subscribe()

    companion object {
        @JvmStatic
        fun createProvider(context: Context): ConnectivityProvider {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ConnectivityProviderImpl(cm)
            } else {
                ConnectivityLegacyProviderImpl(context, cm)
            }
        }
    }
}