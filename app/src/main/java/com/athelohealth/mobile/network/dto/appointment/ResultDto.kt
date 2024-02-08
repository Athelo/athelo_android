package com.athelohealth.mobile.network.dto.appointment


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("photo")
    val photo: String?,
    @SerialName("provider_type")
    val providerType: String?
)