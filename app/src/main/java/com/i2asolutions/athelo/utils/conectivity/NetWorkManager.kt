package com.i2asolutions.athelo.utils.conectivity

import kotlinx.coroutines.flow.MutableStateFlow

const val CONNECTED = 1
const val DISCONNECTED = 0
const val UNKNOWN = -1

object NetWorkManager {
    var networkStatus: Int = UNKNOWN
        set(value) {
            field = value
            networkCallback.tryEmit(value)
        }

    val networkCallback = MutableStateFlow(UNKNOWN)

    val isDisconnected: Boolean
        get() = networkStatus == DISCONNECTED
}

class NetWorkDisconnectedException : Exception()

typealias NetworkCallback = (Int) -> Unit