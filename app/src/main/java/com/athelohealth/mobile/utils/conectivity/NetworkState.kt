@file:Suppress("DEPRECATION")

package com.athelohealth.mobile.utils.conectivity

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkInfo

sealed interface NetworkState {
    object NoInternetConnection : NetworkState
    sealed class ConnectedState(val hasInternet: Boolean) : NetworkState {
        data class Connected(val capabilities: NetworkCapabilities) :
            ConnectedState(capabilities.hasCapability(NET_CAPABILITY_INTERNET))

        data class Legacy(val networkInfo: NetworkInfo) :
            ConnectedState(networkInfo.isConnectedOrConnecting)
    }
}