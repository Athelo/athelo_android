package com.athelohealth.mobile.useCase.appointment

import com.athelohealth.mobile.network.dto.appointment.toProviders
import com.athelohealth.mobile.network.repository.appointment.AppointmentRepository
import com.athelohealth.mobile.presentation.model.appointment.Providers
import com.athelohealth.mobile.presentation.model.base.PageResponse
import javax.inject.Inject

class LoadProvidersUseCase @Inject constructor(private val repository: AppointmentRepository) {

     suspend operator fun invoke(): PageResponse<Providers> {
          return repository.loadProviders().toPageResponse {
               it.toProviders()
          }
     }
}