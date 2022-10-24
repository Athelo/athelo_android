package com.i2asolutions.athelo.network.repository.health

import androidx.annotation.IntRange
import com.i2asolutions.athelo.extensions.parseResponseWithoutBody
import com.i2asolutions.athelo.network.api.HealthApi
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.exercise.ExerciseListDto
import com.i2asolutions.athelo.network.dto.health.*
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateEntryDto
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateRecordDto
import com.i2asolutions.athelo.network.dto.hrv.HrvEntryDto
import com.i2asolutions.athelo.network.dto.sleep.SleepEntryDto
import com.i2asolutions.athelo.network.dto.step.StepEntryDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager

class HealthRepositoryImpl constructor(userManager: UserManager) :
    BaseRepository<HealthApi>(userManager = userManager, clazz = HealthApi::class.java),
    HealthRepository {

    override suspend fun connectFitbitAccount(): InitDto {
        return service.postFitbitAuthInit()
    }

    override suspend fun loadWellbeingForDay(
        day: Int,
        month: Int,
        year: Int
    ): PageResponseDto<WellbeingDto> {
        return service.getUserFeeling(year, month, day)
    }

    override suspend fun loadWellbeingForDay(nextUrl: String): PageResponseDto<WellbeingDto> {
        return service.getUserFeeling(nextUrl)
    }

    override suspend fun createWellbeingForDay(
        date: String,
        @IntRange(from = 1L, to = 1L) feeling: Int
    ): WellbeingDto {
        return service.postUserFeeling(
            mapOf(
                "occurrence_date" to date,
                "general_feeling" to feeling.toString()
            )
        )
    }

    override suspend fun addSymptom(symptomId: Int, comment: String, date: String): UserSymptomDto {
        return service.postUserSymptoms(
            mapOf(
                "occurrence_date" to date,
                "symptom_id" to symptomId.toString(),
                "note" to comment
            )
        )
    }

    override suspend fun removeSymptom(symptomId: Int): Boolean {
        return service.deleteUserSymptoms(symptomId).isSuccessful
    }

    override suspend fun loadMySymptoms(
        day: Int,
        month: Int,
        year: Int
    ): PageResponseDto<UserSymptomDto> {
        return service.getUserSymptoms(year, month, day)
    }

    override suspend fun loadMySymptoms(nextUrl: String): PageResponseDto<UserSymptomDto> {
        return service.getUserSymptoms(nextUrl)
    }

    override suspend fun loadSymptoms(nextUrl: String?): PageResponseDto<SymptomDto> {
        return if (nextUrl.isNullOrBlank()) service.getSymptoms() else service.getSymptoms(nextUrl)
    }

    override suspend fun loadMySymptom(id: Int): UserSymptomDto {
        return service.getUserSymptoms(id)
    }

    override suspend fun loadSymptom(id: Int): SymptomDto {
        return service.getSymptom(id)
    }

    override suspend fun loadSymptomsSummary(): List<SymptomSummaryDto> {
        return service.getLoadSymptomsSummary()
    }

    override suspend fun loadMySymptomsChronology(nextUrl: String): PageResponseDto<UserSymptomDto> {
        return service.getUserSymptoms(nextUrl)
    }

    override suspend fun loadMySymptomsChronology(symptomId: Int): PageResponseDto<UserSymptomDto> {
        return service.getUserSymptomsChronology(symptomId)
    }

    override suspend fun loadSymptomsChronology(nextUrl: String?): PageResponseDto<SymptomChronologyDto> {
        return nextUrl?.let { url -> service.getSymptomChronology(url) }
            ?: service.getSymptomChronology()
    }

    override suspend fun loadFitbitAccount(): PageResponseDto<FitbitProfileDto> {
        return service.getFitbitProfile()
    }

    override suspend fun disconnectFitbitAccount(id: Int): Boolean {
        return service.deleteFitbitProfile(id).parseResponseWithoutBody()
    }

    override suspend fun getSleepRecordsForDay(date: String): List<SleepEntryDto> {
        return service.getSleepRecords(date = date)
    }

    override suspend fun getSleepRecordsForPeriod(
        startDate: String,
        endDate: String
    ): List<SleepEntryDto> {
        return service.getSleepRecords(startDate = startDate, endDate = endDate)
    }

    override suspend fun getStepRecordsForDay(date: String): List<StepEntryDto> {
        return service.getStepRecords(
            date = date,
            body = mapOf("aggregation_function" to "SUM", "interval_function" to "HOUR")
        )
    }

    override suspend fun getStepRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String
    ): List<StepEntryDto> {
        return service.getStepRecords(
            startDate = startDate,
            endDate = endDate,
            body = mapOf("aggregation_function" to "SUM", "interval_function" to intervalFunction)
        )
    }

    override suspend fun getHeartRateRecordsForDay(date: String): List<HeartRateEntryDto> {
        return service.getHeartRateRecords(
            date = date,
            body = mapOf("aggregation_function" to "AVG", "interval_function" to "HOUR")
        )
    }

    override suspend fun getHeartRateRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String
    ): List<HeartRateEntryDto> {
        return service.getHeartRateRecords(
            startDate = startDate,
            endDate = endDate,
            body = mapOf("aggregation_function" to "AVG", "interval_function" to intervalFunction)
        )
    }

    override suspend fun getHeartRateSingleRecordsForDay(date: String): PageResponseDto<HeartRateRecordDto> {
        return service.getHeartRateSingleRecordsForDate(date = date)
    }

    override suspend fun getHeartRateSingleRecordsForUrl(url: String): PageResponseDto<HeartRateRecordDto> {
        return service.getHeartRateSingleRecordsForUrl(url = url)
    }

    override suspend fun getHrvRecordsForDay(date: String): List<HrvEntryDto> {
        return service.getHrvRecords(
            date = date,
            body = mapOf("aggregation_function" to "SUM", "interval_function" to "HOUR")
        )
    }

    override suspend fun getHrvRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String,
    ): List<HrvEntryDto> {
        return service.getHrvRecords(
            startDate = startDate,
            endDate = endDate,
            body = mapOf("aggregation_function" to "AVG", "interval_function" to intervalFunction)
        )
    }

    override suspend fun getExerciseRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String
    ): ExerciseListDto {
        return service.getExerciseRecords(
            startDate = startDate,
            endDate = endDate,
            body = mapOf("aggregation_function" to "SUM", "interval_function" to intervalFunction)
        )
    }
}