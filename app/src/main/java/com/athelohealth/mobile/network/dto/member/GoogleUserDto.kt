package com.athelohealth.mobile.network.dto.member


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleUserDto(
    @SerialName("email")
    val email: String? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("locale")
    val locale: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("picture")
    val picture: String? = null,
    @SerialName("verified_email")
    val verifiedEmail: Boolean? = null
)