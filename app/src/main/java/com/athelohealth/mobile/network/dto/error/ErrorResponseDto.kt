package com.athelohealth.mobile.network.dto.error

import com.athelohealth.mobile.presentation.model.error.ErrorResponse
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

@Serializable
class ErrorResponseDto(
    @SerialName("detail") val details: String? = null,
    @SerialName("details_json") val detailsJson: ErrorDetails? = null,
    @SerialName("error_code") val errorCode: Int? = null,
    @SerialName("extra") val extra: ErrorExtraDto? = null
) {
    fun toErrorResponse(): ErrorResponse {
        val extraMessage = extra?.getMessage()
        val detailsJsonString = detailsJson?.getMessage()
        return ErrorResponse(
            if (!extraMessage.isNullOrBlank()) extraMessage
            else if (!detailsJsonString.isNullOrBlank()) detailsJsonString
            else details ?: ""
        )
    }
}

@Serializable(with = ErrorDetailsSerializer::class)
class ErrorDetails(val keys: Map<String, List<String>>) {
    fun getMessage(): String? {
        return when {
            keys.isNotEmpty() -> keys.map { it.value.joinToString(" ") }
                .joinToString(" ")
            else -> null
        }
    }
}


object ErrorDetailsSerializer : KSerializer<ErrorDetails> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("com.athelohealth.mobile.network.model.error.ErrorDetails") { }

    override fun deserialize(decoder: Decoder): ErrorDetails {
        return try {
            val element: JsonElement = (decoder as JsonDecoder).decodeJsonElement()
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
            }
            ErrorDetails(map)
        } catch (e: Exception) {
            ErrorDetails(emptyMap())
        }
    }

    override fun serialize(encoder: Encoder, value: ErrorDetails) {
    }
}