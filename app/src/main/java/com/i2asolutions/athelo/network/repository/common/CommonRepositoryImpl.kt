package com.i2asolutions.athelo.network.repository.common

import com.i2asolutions.athelo.network.api.CommonApi
import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.network.dto.enums.EnumsDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager
import okhttp3.MultipartBody
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(userManager: UserManager) :
    BaseRepository<CommonApi>(CommonApi::class.java, userManager),
    CommonRepository {

    override suspend fun updateUserImage(body: MultipartBody.Part): ImageDto {
        return service.uploadImage(body)
    }

    override suspend fun getEnums(): EnumsDto {
        return service.getEnums()
    }
}