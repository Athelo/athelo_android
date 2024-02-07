package com.athelohealth.mobile.network.dto.appointment

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class PatientProviderDto(
    val display_name: String?,
    val photo: String?
)
