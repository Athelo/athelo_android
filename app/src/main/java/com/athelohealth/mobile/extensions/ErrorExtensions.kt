package com.athelohealth.mobile.extensions

import com.athelohealth.mobile.utils.consts.Const.NOT_AVAILABLE_CONTENT_ERROR_MESSAGE
import com.athelohealth.mobile.utils.consts.Const.UNIVERSAL_ERROR_MESSAGE
import com.athelohealth.mobile.utils.parse.ErrorParser
import retrofit2.HttpException

fun HttpException.parseMessage(): String =
    response()?.errorBody()
        ?.let {
            ErrorParser.parse(it.string()).message
        }
        ?: NOT_AVAILABLE_CONTENT_ERROR_MESSAGE
val Throwable.errorMessageOrUniversalMessage: String
    get() = localizedMessage?.ifEmpty { UNIVERSAL_ERROR_MESSAGE } ?: UNIVERSAL_ERROR_MESSAGE