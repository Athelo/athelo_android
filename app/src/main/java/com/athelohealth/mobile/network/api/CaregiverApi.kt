package com.athelohealth.mobile.network.api

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.caregiver.CaregiverRelationshipDto
import com.athelohealth.mobile.network.dto.caregiver.InvitationDto
import retrofit2.Response
import retrofit2.http.*

interface CaregiverApi {

    @DELETE("api/v1/health/caregivers/{caregiver_id}/")
    suspend fun deleteCaregiver(@Path("caregiver_id") caregiverId: Int): Response<Unit>

    @GET
    suspend fun getCaregivers(@Url url: String = "api/v1/health/caregivers/"): PageResponseDto<CaregiverRelationshipDto>

    @GET
    suspend fun getPatients(@Url url: String = "api/v1/health/patients/"): PageResponseDto<CaregiverRelationshipDto>

    @DELETE("api/v1/health/patients/{patient_id}/")
    suspend fun deletePatient(@Path("patient_id") patientId: String): Response<Unit>

    @POST
    suspend fun postCaregiverInvite(
        @Url url: String = "api/v1/health/caregiver_invitations/invite/",
        @Body body: Map<String, String>
    ): Response<Unit>

    @POST("api/v1//health/caregiver_invitations/consume/{code}/")
    suspend fun postCaregiverInviteConsume(
        @Path("code") code: String,
        @Body body: Map<String, String>
    ): Response<Unit>

    @GET("api/v1/health/caregiver_invitations/")
    suspend fun getPendingInvitations(@Query("status") status: String = "sent"): PageResponseDto<InvitationDto>

    @GET
    suspend fun getPendingInvitationsNext(@Url url: String = "api/v1/health/caregiver_invitations/"): PageResponseDto<InvitationDto>

    @POST("api/v1/health/caregiver_invitations/{id}/cancel/")
    suspend fun postCancelPendingInvitations(@Path("id") id: Int): Response<Unit>
}