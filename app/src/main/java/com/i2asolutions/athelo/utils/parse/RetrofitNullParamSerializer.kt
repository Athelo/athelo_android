@file:OptIn(ExperimentalSerializationApi::class)

package com.i2asolutions.athelo.utils.parse

import com.i2asolutions.athelo.network.dto.base.RetrofitNullableStringParam
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.jsonPrimitive

class RetrofitNullParamSerializer : KSerializer<RetrofitNullableStringParam> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("com.i2asolutions.athelo.utils.parse.RetrofitNullParamSerializer")

    override fun deserialize(decoder: Decoder): RetrofitNullableStringParam {
        val jsonDecoder = (decoder as JsonDecoder)
        val primitive = jsonDecoder.decodeJsonElement().jsonPrimitive
        return RetrofitNullableStringParam(primitive.content)
    }

    override fun serialize(encoder: Encoder, value: RetrofitNullableStringParam) {
        val jsonEncoder = encoder as JsonEncoder
        val param = value.param
        if (param.isNullOrBlank())
            jsonEncoder.encodeNull()
        else
            jsonEncoder.encodeString(value = param)
    }
}