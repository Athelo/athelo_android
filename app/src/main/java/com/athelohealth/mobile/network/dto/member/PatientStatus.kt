package com.athelohealth.mobile.network.dto.member

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PatientStatus(
    @SerialName("active")
    val active: Boolean? = null,

    @SerialName("cancer_status")
    val cancerStatus: CancerStatus? = null,

    @SerialName("id")
    val id: Int? = null
)
