package com.athelohealth.mobile.presentation.model.appointment

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class ProvidersAvailability(
    val next: String?,
    val results: List<String>?
)
