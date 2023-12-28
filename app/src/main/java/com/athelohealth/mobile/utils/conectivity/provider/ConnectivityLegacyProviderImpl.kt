@file:Suppress("DEPRECATION")

package com.i2asolutions.sff.utils.internetStatus.provider

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.athelohealth.mobile.utils.conectivity.NetworkState

class ConnectivityLegacyProviderImpl(
    private val context: Context,
    private val cm: ConnectivityManager
) : ConnectivityBaseProviderImpl() {
    private val receiver = ConnectivityReceiver()
    override fun subscribeListener() {
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override val networkState: NetworkState
        get() {
            val activeNetworkInfo = cm.activeNetworkInfo
            return if (activeNetworkInfo != null) {
                NetworkState.ConnectedState.Legacy(activeNetworkInfo)
            } else {
                NetworkState.NoInternetConnection
            }
        }

    private inner class ConnectivityReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
            // on some devices ConnectivityManager.getActiveNetworkInfo() does not provide the correct network state
            val networkInfo = cm.activeNetworkInfo
            val fallbackNetworkInfo: NetworkInfo? =
                intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO)
            // a set of dirty workarounds
            val state: NetworkState =
                if (networkInfo?.isConnectedOrConnecting == true) {
                    NetworkState.ConnectedState.Legacy(networkInfo)
                } else if (networkInfo != null && fallbackNetworkInfo != null &&
                    networkInfo.isConnectedOrConnecting != fallbackNetworkInfo.isConnectedOrConnecting
                ) {
                    NetworkState.ConnectedState.Legacy(
                        fallbackNetworkInfo
                    )
                } else {
                    val state = networkInfo ?: fallbackNetworkInfo
                    if (state != null) NetworkState.ConnectedState.Legacy(
                        state
                    ) else NetworkState.NoInternetConnection
                }
            dispatchChange(state)
        }
    }
}