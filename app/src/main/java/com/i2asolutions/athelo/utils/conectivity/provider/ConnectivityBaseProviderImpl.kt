package com.i2asolutions.sff.utils.internetStatus.provider

import com.i2asolutions.athelo.utils.conectivity.CONNECTED
import com.i2asolutions.athelo.utils.conectivity.DISCONNECTED
import com.i2asolutions.athelo.utils.conectivity.NetWorkManager
import com.i2asolutions.athelo.utils.conectivity.NetworkState

sealed class ConnectivityBaseProviderImpl : ConnectivityProvider {
    override fun subscribe() {
        subscribeListener()
        dispatchChange(networkState)
    }

    protected fun dispatchChange(state: NetworkState) {
        val networkState = if (state.hasInternet) CONNECTED else DISCONNECTED
        if (NetWorkManager.networkStatus != networkState) {
            NetWorkManager.networkStatus = networkState
        }
    }

    private val NetworkState.hasInternet: Boolean
        get() = (this as? NetworkState.ConnectedState)?.hasInternet == true

    protected abstract fun subscribeListener()
}