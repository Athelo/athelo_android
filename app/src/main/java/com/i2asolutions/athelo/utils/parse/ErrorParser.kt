package com.i2asolutions.athelo.utils.parse

import com.i2asolutions.athelo.network.dto.error.ErrorResponseDto
import com.i2asolutions.athelo.presentation.model.error.ErrorResponse
import com.i2asolutions.athelo.utils.consts.Const
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
