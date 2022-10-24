package com.i2asolutions.athelo.network.repository.deviceConfig

import com.i2asolutions.athelo.network.api.DeviceConfigApi
import com.i2asolutions.athelo.network.dto.device.DeviceConfigDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager

class DeviceConfigRepositoryImpl constructor(userManager: UserManager) :
    BaseRepository<DeviceConfigApi>(userManager = userManager, clazz = DeviceConfigApi::class.java),
    DeviceConfigRepository {

    override suspend fun loadDeviceConfig(): DeviceConfigDto {
        return service.getMyDeviceConfigs()
    }
}