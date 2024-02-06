package com.athelohealth.mobile.useCase.appointment

import com.athelohealth.mobile.network.repository.appointment.AppointmentRepository
import com.athelohealth.mobile.presentation.model.appointment.Providers
import com.athelohealth.mobile.presentation.model.appointment.ProvidersAvailability
import javax.inject.Inject

class LoadProvidersUseCase @Inject constructor(private val repository: AppointmentRepository) {
     suspend operator fun invoke(): Providers {
          return repository.loadProviders().toProviders()
     }

     suspend operator fun invoke(date: String): ProvidersAvailability {
          return repository.getProvidersAvailability(date).toProvidersAvailability()
     }
}