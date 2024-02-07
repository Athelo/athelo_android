package com.athelohealth.mobile.network.dto.appointment


import androidx.annotation.Keep
import com.athelohealth.mobile.presentation.model.appointment.ProvidersAvailability
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class ProvidersAvailabilityDto(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<List<String>>?
) {

    fun toProvidersAvailability(): ProvidersAvailability {
        return ProvidersAvailability(
            next = next,
            results = results?.firstOrNull()?.map {
                it.substringAfter(" ")
            }
        )
    }
}