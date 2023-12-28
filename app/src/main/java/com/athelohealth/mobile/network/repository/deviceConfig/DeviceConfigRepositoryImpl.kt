package com.athelohealth.mobile.network.repository.deviceConfig

import com.athelohealth.mobile.network.api.DeviceConfigApi
import com.athelohealth.mobile.network.dto.device.DeviceConfigDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager

class DeviceConfigRepositoryImpl constructor(userManager: UserManager) :
    BaseRepository<DeviceConfigApi>(userManager = userManager, clazz = DeviceConfigApi::class.java),
    DeviceConfigRepository {

    override suspend fun loadDeviceConfig(): DeviceConfigDto {
        return service.getMyDeviceConfigs()
    }
}