package com.athelohealth.mobile.network.dto.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SocialAppUserDto(
    @SerialName("i2a_identifier") val i2aIdentifier: String? = null,
    @SerialName("i2a_username") val i2aUsername: String? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("username") val username: String
)