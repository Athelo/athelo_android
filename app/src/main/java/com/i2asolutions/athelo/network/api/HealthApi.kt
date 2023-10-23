package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.exercise.ExerciseListDto
import com.i2asolutions.athelo.network.dto.health.*
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateEntryDto
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateRecordDto
import com.i2asolutions.athelo.network.dto.hrv.HrvEntryDto
import com.i2asolutions.athelo.network.dto.sleep.SleepEntryDto
import com.i2asolutions.athelo.network.dto.step.StepEntryDto
import retrofit2.Response
import retrofit2.http.*

interface HealthApi {
    @POST("api/v1/health/integrations/fitbit/authorize/initialize/")
    suspend fun postFitbitAuthInit(): InitDto

    @POST("api/v1/health/user_feeling/")
    suspend fun postUserFeeling(@Body map: Map<String, String>): WellbeingDto

    @GET("api/v1/health/user_feeling/")
    suspend fun getUserFeeling(
        @Query("occurrence_date__year") year: Int,
        @Query("occurrence_date__month") month: Int,
        @Query("occurrence_date__day") day: Int
    ): PageResponseDto<WellbeingDto>

    @GET
    suspend fun getUserFeeling(@Url url: String): PageResponseDto<WellbeingDto>

    @GET
    suspend fun getSymptoms(@Url url: String = "api/v1/health/symptoms/"): PageResponseDto<SymptomDto>

    @GET("api/v1/health/user_symptoms/")
    suspend fun getUserSymptoms(
        @Query("occurrence_date__year") year: Int,
        @Query("occurrence_date__month") month: Int,
        @Query("occurrence_date__day") day: Int
    ): PageResponseDto<UserSymptomDto>

    @GET
    suspend fun getUserSymptoms(@Url url: String = "api/v1/health/user_symptoms/"): PageResponseDto<UserSymptomDto>

    @GET("api/v1/health/user_symptoms/{id}/")
    suspend fun getUserSymptoms(@Path("id") id: Int): UserSymptomDto

    @GET("api/v1/health/symptoms/{id}/")
    suspend fun getSymptom(@Path("id") id: Int): SymptomDto

    @POST("api/v1/health/user_symptoms/")
    suspend fun postUserSymptoms(@Body map: Map<String, String>): UserSymptomDto

    @DELETE("api/v1/health/user_symptoms/{id}/")
    suspend fun deleteUserSymptoms(@Path("id") id: Int): Response<Unit>

    @GET("api/v1/health/user_symptoms/summary/")
    suspend fun getLoadSymptomsSummary(): List<SymptomSummaryDto>

    @GET("api/v1/health/user_symptoms/")
    suspend fun getUserSymptomsChronology(@Query("symptom__id") id: Int): PageResponseDto<UserSymptomDto>

    @GET
    suspend fun getSymptomChronology(@Url url: String = "api/v1/health/user_feelings_and_symptoms_per_day/?by_symptoms=true"): PageResponseDto<SymptomChronologyDto>

    @GET("api/v1/health/integrations/fitbit/profile/")
    suspend fun getFitbitProfile(@Query("force") force: Boolean? = null): PageResponseDto<FitbitProfileDto>

    @HTTP(method = "DELETE", path = "api/v1/health/integrations/fitbit/profile/{id}/", hasBody = true)
    suspend fun deleteFitbitProfile(
        @Path("id") id: Int,
        @Body body: Map<String, Boolean>
    ): Response<Unit>

    @POST("api/v1/health/sleep_record/generate-level-aggregation-dashboard/")
    suspend fun getSleepRecords(
        @Query("collected_at_date") date: String? = null,
        @Query("collected_at_date__gte") startDate: String? = null,
        @Query("collected_at_date__lte") endDate: String? = null,
        @Query("collected_at_date__gt") afterDate: String? = null,
        @Query("collected_at_date__lt") beforeDate: String? = null,
        @Query("_patient_id") patientId: Int? = null,
        @Body body: Map<String, String> = mapOf(
            "aggregation_function" to "SUM",
            "interval_function" to "DAY",
        )
    ): List<SleepEntryDto>

    @POST("api/v1/health/steps_record/generate-dashboard/")
    suspend fun getStepRecords(
        @Query("collected_at_date") date: String? = null,
        @Query("collected_at_date__gte") startDate: String? = null,
        @Query("collected_at_date__lte") endDate: String? = null,
        @Query("collected_at_date__gt") afterDate: String? = null,
        @Query("collected_at_date__lt") beforeDate: String? = null,
        @Query("_patient_id") patientId: Int? = null,
        @Body body: Map<String, String> = mapOf(
            "aggregation_function" to "SUM",
            "interval_function" to "DAY",
        )
    ): List<StepEntryDto>

    @POST("api/v1/health/heart_rate_record/generate-dashboard/")
    suspend fun getHeartRateRecords(
        @Query("collected_at_date") date: String? = null,
        @Query("collected_at_date__gte") startDate: String? = null,
        @Query("collected_at_date__lte") endDate: String? = null,
        @Query("collected_at_date__gt") afterDate: String? = null,
        @Query("collected_at_date__lt") beforeDate: String? = null,
        @Query("_patient_id") patientId: Int? = null,
        @Body body: Map<String, String> = mapOf(
            "aggregation_function" to "SUM",
            "interval_function" to "DAY",
        )
    ): List<HeartRateEntryDto>

    @POST("api/v1/health/heart_rate_variability_record/generate-dashboard/")
    suspend fun getHrvRecords(
        @Query("collected_at_date") date: String? = null,
        @Query("collected_at_date__gte") startDate: String? = null,
        @Query("collected_at_date__lte") endDate: String? = null,
        @Query("collected_at_date__gt") afterDate: String? = null,
        @Query("collected_at_date__lt") beforeDate: String? = null,
        @Query("_patient_id") patientId: Int? = null,
        @Body body: Map<String, String> = mapOf(
            "aggregation_function" to "SUM",
            "interval_function" to "DAY",
        )
    ): List<HrvEntryDto>

    @GET("api/v1/health/heart_rate_record/")
    suspend fun getHeartRateSingleRecordsForDate(
        @Query("collected_at_date") date: String,
        @Query("page_size") pageSize: Int = 100,
        @Query("_patient_id") patientId: Int? = null,
    ): PageResponseDto<HeartRateRecordDto>

    @GET
    suspend fun getHeartRateSingleRecordsForUrl(
        @Url url: String,
        @Query("_patient_id") patientId: Int? = null,
    ): PageResponseDto<HeartRateRecordDto>

    @POST("api/v1/health/activity/generate-dashboard/")
    suspend fun getExerciseRecords(
        @Query("start_date") startDate: String? = null,
        @Query("end_date") endDate: String? = null,
        @Query("_patient_id") patientId: Int? = null,
        @Body body: Map<String, String> = mapOf(
            "aggregation_function" to "SUM",
            "interval_function" to "DAY",
        )
    ): ExerciseListDto
}