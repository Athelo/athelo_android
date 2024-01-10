package com.athelohealth.mobile.utils.conectivity.provider

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.athelohealth.mobile.utils.conectivity.NetworkState

@RequiresApi(Build.VERSION_CODES.N)
class ConnectivityProviderImpl(private val cm: ConnectivityManager) :
    ConnectivityBaseProviderImpl() {
    private val networkCallback = ConnectivityCallback()
    override fun subscribeListener() {
        cm.runCatching { registerDefaultNetworkCallback(networkCallback) }
    }

    override val networkState: NetworkState
        get() {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            return if (capabilities != null) {
                NetworkState.ConnectedState.Connected(capabilities)
            } else {
                NetworkState.NoInternetConnection
            }
        }

    private inner class ConnectivityCallback : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            dispatchChange(NetworkState.ConnectedState.Connected(capabilities))
        }

        override fun onLost(network: Network) {
            dispatchChange(NetworkState.NoInternetConnection)
        }
    }
}