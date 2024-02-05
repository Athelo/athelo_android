package com.athelohealth.mobile.network.dto.appointment


import com.athelohealth.mobile.presentation.model.appointment.Provider
import com.athelohealth.mobile.presentation.model.appointment.Providers
import kotlinx.serialization.Serializable

@Serializable
data class ProvidersDto(
    val count: Int?,
    val results: List<ResultDto>?,
    val next: String?,
    val previous: String?
)

fun ProvidersDto.toProviders() = Providers(
    count = this.count,
    providers = this.results?.map {
        it.toProvider()
    }
)

fun ResultDto.toProvider() = Provider(
    displayName = this.display_name,
    id = this.id,
    photo = this.photo,
    providerType = this.provider_type
)