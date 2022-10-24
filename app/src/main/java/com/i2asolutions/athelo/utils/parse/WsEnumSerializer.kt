package com.i2asolutions.athelo.utils.parse

import com.i2asolutions.athelo.network.dto.enums.EnumItemDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


class WsEnumSerializer : KSerializer<EnumItemDto> {

    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("com.i2asolutions.athelo.utils.parse.WsEnumSerializer")

    override fun deserialize(decoder: Decoder): EnumItemDto {
        val jsonDecoder = (decoder as JsonDecoder)
        val list = jsonDecoder.decodeJsonElement().jsonArray

        val first = list[0].jsonPrimitive
        val second = list[1].jsonPrimitive
        return if (first.isString) EnumItemDto(first.content, second.content)
        else EnumItemDto(first.int.toString(), second.content)
    }

    override fun serialize(encoder: Encoder, value: EnumItemDto) {
        val jsonEncoder = encoder as JsonEncoder
        jsonEncoder.encodeJsonElement(buildJsonArray {
            add(value.id)
            add(value.label)
        })
    }
}