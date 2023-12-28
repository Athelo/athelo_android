package com.athelohealth.mobile.network.dto.base

import com.athelohealth.mobile.utils.parse.RetrofitNullParamSerializer
import kotlinx.serialization.Serializable

@Serializable(RetrofitNullParamSerializer::class)
class RetrofitNullableStringParam(val param: String?) {
}