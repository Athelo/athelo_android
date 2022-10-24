package com.i2asolutions.athelo.network.repository.deviceConfig

import com.i2asolutions.athelo.network.dto.device.DeviceConfigDto

interface DeviceConfigRepository {
    suspend fun loadDeviceConfig(): DeviceConfigDto
}