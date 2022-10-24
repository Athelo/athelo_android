package com.i2asolutions.athelo.extensions

import retrofit2.HttpException
import retrofit2.Response

@Throws(HttpException::class)
fun<T> Response<T>.getBodyOrThrow():T {
    return body() ?: throw HttpException(this)
}

@Throws(HttpException::class)
fun<T> Response<T>.parseResponseWithoutBody(): Boolean {
    return if(isSuccessful) true else throw HttpException(this)
}