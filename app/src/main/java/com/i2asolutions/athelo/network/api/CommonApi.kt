package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.network.dto.enums.EnumsDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CommonApi {
    @Multipart
    @POST("api/v1/common/image/")
    suspend fun uploadImage(@Part file: MultipartBody.Part): ImageDto

    @GET("api/v1/common/enums/")
    suspend fun getEnums(): EnumsDto
}