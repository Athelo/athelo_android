package com.i2asolutions.athelo.network.dto.base

import com.i2asolutions.athelo.utils.parse.RetrofitNullParamSerializer
import kotlinx.serialization.Serializable

@Serializable(RetrofitNullParamSerializer::class)
class RetrofitNullableStringParam(val param: String?) {
}