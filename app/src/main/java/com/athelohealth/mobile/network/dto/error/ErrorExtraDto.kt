package com.athelohealth.mobile.network.dto.error

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


@Serializable(with = ErrorExtraSerializer::class)
class ErrorExtraDto(
    val detail: String? = null,
    val detailMap: Map<String, List<String>>? = null,
    val errorCode: Int? = null
) {
    fun getMessage(): String? {
        return when {
            detail != null -> detail
            detailMap != null -> detailMap.map { it.value.joinToString(" ") }
                .joinToString(" ")
            else -> null
        }
    }
}

object ErrorExtraSerializer : KSerializer<ErrorExtraDto> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.i2asolutions.sff.network.model.error.ErrorDetails") {
            element<JsonElement>("exception")
        }

    override fun deserialize(decoder: Decoder): ErrorExtraDto {
        return try {
            var detail: String? = null
            val detailMap: HashMap<String, List<String>> = hashMapOf()
            var errorCode: Int? = null
            val element: JsonObject? =
                ((decoder as JsonDecoder).decodeJsonElement() as? JsonObject)?.get("exception") as? JsonObject
            if (element != null) {
                val detailElement = element["detail"]
                if (detailElement is JsonPrimitive) {
                    detail = detailElement.contentOrNull
                } else if (detailElement is JsonObject) {
                    detailElement.keys.forEach { key ->
                        val array = detailElement[key] as? JsonArray
                        val list = arrayListOf<String?>()
                        array?.forEach {
                            if (it is JsonPrimitive) list.add(it.contentOrNull)
                        }
                        detailMap[key] = list.filterNotNull()
                    }
                }
                val errorCodeElement = element["error_code"] as? JsonPrimitive
                errorCode = errorCodeElement?.intOrNull
            }
            val map = hashMapOf<String, List<String>>()
            if (element is JsonObject) {
                element.keys.forEach { key ->
                    val array = element[key] as? JsonArray
                    val list = arrayListOf<String?>()
                    array?.forEach {
                        if (it is JsonPrimitive) list.add(it.contentOrNull)
                    }
                    map[key] = list.filterNotNull()
                }
                System.err.println()
            }
            ErrorExtraDto(detail, detailMap, errorCode)
        } catch (e: Exception) {
            ErrorExtraDto()
        }
    }

    override fun serialize(encoder: Encoder, value: ErrorExtraDto) {
    }
}