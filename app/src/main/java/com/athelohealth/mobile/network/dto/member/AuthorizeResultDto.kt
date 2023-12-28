package com.athelohealth.mobile.network.dto.member

import com.athelohealth.mobile.presentation.model.member.AuthorizeResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthorizeResultDto(
    @SerialName("first_time_login") val firstTime: Boolean = false,
    @SerialName("token_data") val tokenData: TokenDto
) {
    fun toAuthorizeResult(): AuthorizeResult {
        return AuthorizeResult(firstTime, tokenData.toToken())
    }
}
