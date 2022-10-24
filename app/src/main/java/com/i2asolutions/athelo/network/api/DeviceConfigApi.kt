package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.device.DeviceConfigDto
import retrofit2.http.GET

interface DeviceConfigApi {
    @GET("api/v1/device_config/get_my_device_configs/")
    suspend fun getMyDeviceConfigs(): DeviceConfigDto
}