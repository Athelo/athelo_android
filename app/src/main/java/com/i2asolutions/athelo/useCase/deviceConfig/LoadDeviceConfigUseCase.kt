package com.i2asolutions.athelo.useCase.deviceConfig

import com.i2asolutions.athelo.network.repository.deviceConfig.DeviceConfigRepository
import com.i2asolutions.athelo.presentation.ui.device.DeviceConfig
import javax.inject.Inject

class LoadDeviceConfigUseCase @Inject constructor(private val repository: DeviceConfigRepository) {

    suspend operator fun invoke(): DeviceConfig {
        return repository.loadDeviceConfig().toDeviceConfig()
    }
}