package com.athelohealth.mobile.extensions

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

fun debugPrintWithTag(tag:String, vararg messages: Any?) {
    messages.forEach {
        Timber.tag(tag).d(it.toString())
    }
}