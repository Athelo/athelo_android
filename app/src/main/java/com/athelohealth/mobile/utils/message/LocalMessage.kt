package com.athelohealth.mobile.utils.message

sealed interface LocalMessage {
    @JvmInline
    value class Success(val message: String) : LocalMessage

    @JvmInline
    value class Error(val message: String) : LocalMessage

    @JvmInline
    value class Normal(val message: String) : LocalMessage
}