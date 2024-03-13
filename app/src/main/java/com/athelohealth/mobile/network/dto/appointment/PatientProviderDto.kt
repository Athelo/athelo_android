package com.athelohealth.mobile.network.dto.appointment

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class PatientProviderDto(
    @SerialName("display_name")
    val displayName: String?,
    @SerialName("photo")
    val photo: String?
)
