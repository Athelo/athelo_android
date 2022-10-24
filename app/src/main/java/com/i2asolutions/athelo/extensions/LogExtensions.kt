package com.i2asolutions.athelo.extensions

import timber.log.Timber

fun debugPrint(vararg messages: Any?) {
    messages.forEach {
        Timber.d(it.toString())
    }
}

fun errorPrint(vararg messages: String?) {
    messages.forEach {
        Timber.e(it)
    }
}