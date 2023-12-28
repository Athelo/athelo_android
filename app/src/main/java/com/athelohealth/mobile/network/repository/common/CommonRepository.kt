package com.athelohealth.mobile.network.repository.common

import com.athelohealth.mobile.network.dto.base.ImageDto
import com.athelohealth.mobile.network.dto.enums.EnumsDto
import okhttp3.MultipartBody

interface CommonRepository {

    suspend fun updateUserImage(body: MultipartBody.Part): ImageDto

    suspend fun getEnums(): EnumsDto

}