package com.athelohealth.mobile.utils.parse

import com.athelohealth.mobile.network.dto.error.ErrorResponseDto
import com.athelohealth.mobile.presentation.model.error.ErrorResponse
import com.athelohealth.mobile.utils.consts.Const
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException

object ErrorParser {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun parse(entry: String, fallbackMessage: String = ""): ErrorResponse {
        return try {
            val dto: ErrorResponseDto = json.decodeFromString(entry)
            dto.toErrorResponse()
        } catch (e: Exception) {
            ErrorResponse(fallbackMessage)
        }
    }

    fun parse(
        e: HttpException,
        fallbackMessage: String = Const.UNIVERSAL_ERROR_MESSAGE
    ): ErrorResponse {
        return parse(e.response()?.errorBody()?.string() ?: "", fallbackMessage)
    }
}
