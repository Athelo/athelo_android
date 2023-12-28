package com.athelohealth.mobile.network.repository.deviceConfig

import com.athelohealth.mobile.network.dto.device.DeviceConfigDto

interface DeviceConfigRepository {
    suspend fun loadDeviceConfig(): DeviceConfigDto
}