package com.athelohealth.mobile.network.repository.appointment

import com.athelohealth.mobile.network.dto.appointment.ProvidersDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto

interface AppointmentRepository {

    suspend fun loadProviders(): PageResponseDto<ProvidersDto>
}