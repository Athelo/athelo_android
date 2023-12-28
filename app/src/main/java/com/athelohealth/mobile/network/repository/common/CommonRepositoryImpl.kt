package com.athelohealth.mobile.network.repository.common

import com.athelohealth.mobile.network.api.CommonApi
import com.athelohealth.mobile.network.dto.base.ImageDto
import com.athelohealth.mobile.network.dto.enums.EnumsDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager
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