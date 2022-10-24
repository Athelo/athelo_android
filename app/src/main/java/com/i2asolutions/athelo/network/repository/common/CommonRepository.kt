package com.i2asolutions.athelo.network.repository.common

import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.network.dto.enums.EnumsDto
import okhttp3.MultipartBody

interface CommonRepository {

    suspend fun updateUserImage(body: MultipartBody.Part): ImageDto

    suspend fun getEnums(): EnumsDto

}