package com.athelohealth.mobile.network.dto.member

import com.athelohealth.mobile.presentation.model.member.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TokenDto(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("scope") val scope: String,
    @SerialName("expires_in") val expiresIn: Int = 0
) {
    fun toToken(): Token {
        return Token(
            accessToken = accessToken,
            refreshToken = refreshToken,
            tokenType = tokenType,
            scope = scope,
            expiresIn = expiresIn
        )
    }
}