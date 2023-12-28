package com.athelohealth.mobile.useCase.deviceConfig

import com.athelohealth.mobile.network.repository.deviceConfig.DeviceConfigRepository
import com.athelohealth.mobile.presentation.ui.patient.device.DeviceConfig
import javax.inject.Inject

class LoadDeviceConfigUseCase @Inject constructor(private val repository: DeviceConfigRepository) {

    suspend operator fun invoke(): DeviceConfig {
        return repository.loadDeviceConfig().toDeviceConfig()
    }
}