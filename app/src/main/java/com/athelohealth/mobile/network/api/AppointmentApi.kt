package com.athelohealth.mobile.network.api

import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import retrofit2.http.GET
import retrofit2.http.Url

interface AppointmentApi {

    @GET
    suspend fun loadProviders(
        @Url url: String = "api/v1/providers"
    ): PageResponseDto<ProvidersDto>
}