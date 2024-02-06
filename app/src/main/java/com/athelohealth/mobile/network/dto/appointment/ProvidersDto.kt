package com.athelohealth.mobile.network.dto.appointment


import com.athelohealth.mobile.presentation.model.appointment.Provider
import com.athelohealth.mobile.presentation.model.appointment.Providers
import kotlinx.serialization.Serializable

@Serializable
data class ProvidersDto(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<ResultDto>?
) {

    fun toProviders(): Providers {
        return Providers(
            count = count,
            providers = results?.map {
                it.toProvider()
            }
        )
    }

    private fun ResultDto.toProvider() = Provider(
        displayName = this.display_name,
        id = this.id,
        photo = this.photo,
        providerType = this.provider_type
    )
}