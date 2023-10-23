package com.i2asolutions.athelo.network.repository.health

import androidx.annotation.IntRange
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.exercise.ExerciseListDto
import com.i2asolutions.athelo.network.dto.health.*
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateEntryDto
import com.i2asolutions.athelo.network.dto.heartRate.HeartRateRecordDto
import com.i2asolutions.athelo.network.dto.hrv.HrvEntryDto
import com.i2asolutions.athelo.network.dto.sleep.SleepEntryDto
import com.i2asolutions.athelo.network.dto.step.StepEntryDto

interface HealthRepository {

    suspend fun connectFitbitAccount(): InitDto

    suspend fun loadWellbeingForDay(day: Int, month: Int, year: Int): PageResponseDto<WellbeingDto>

    suspend fun loadWellbeingForDay(nextUrl: String): PageResponseDto<WellbeingDto>

    suspend fun createWellbeingForDay(
        date: String, @IntRange(from = 1L, to = 1L) feeling: Int
    ): WellbeingDto


    suspend fun addSymptom(symptomId: Int, comment: String, date: String): UserSymptomDto
    suspend fun removeSymptom(symptomId: Int): Boolean

    suspend fun loadMySymptomsChronology(symptomId: Int): PageResponseDto<UserSymptomDto>
    suspend fun loadMySymptomsChronology(nextUrl: String): PageResponseDto<UserSymptomDto>

    suspend fun loadMySymptoms(day: Int, month: Int, year: Int): PageResponseDto<UserSymptomDto>
    suspend fun loadMySymptoms(nextUrl: String): PageResponseDto<UserSymptomDto>
    suspend fun loadMySymptom(id: Int): UserSymptomDto
    suspend fun loadSymptom(id: Int): SymptomDto
    suspend fun loadSymptoms(nextUrl: String?): PageResponseDto<SymptomDto>
    suspend fun loadSymptomsSummary(): List<SymptomSummaryDto>
    suspend fun loadSymptomsChronology(nextUrl: String?): PageResponseDto<SymptomChronologyDto>

    suspend fun loadFitbitAccount(): PageResponseDto<FitbitProfileDto>
    suspend fun disconnectFitbitAccount(id: Int, force: Boolean? = null): Boolean

    suspend fun getSleepRecordsForDay(date: String, patientId: Int? = null): List<SleepEntryDto>
    suspend fun getSleepRecordsForPeriod(
        startDate: String,
        endDate: String,
        patientId: Int? = null,
    ): List<SleepEntryDto>

    suspend fun getStepRecordsForDay(
        date: String,
        patientId: Int? = null,
    ): List<StepEntryDto>

    suspend fun getStepRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String = "",
        patientId: Int? = null,
    ): List<StepEntryDto>

    suspend fun getHeartRateRecordsForDay(
        date: String,
        patientId: Int? = null,
    ): List<HeartRateEntryDto>

    suspend fun getHeartRateRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String = "",
        patientId: Int? = null,
    ): List<HeartRateEntryDto>

    suspend fun getHeartRateSingleRecordsForDay(
        date: String,
        patientId: Int? = null,
    ): PageResponseDto<HeartRateRecordDto>

    suspend fun getHeartRateSingleRecordsForUrl(
        url: String,
        patientId: Int? = null,
    ): PageResponseDto<HeartRateRecordDto>

    suspend fun getHrvRecordsForDay(date: String, patientId: Int? = null): List<HrvEntryDto>
    suspend fun getHrvRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String = "",
        patientId: Int? = null,
    ): List<HrvEntryDto>

    suspend fun getExerciseRecordsForPeriod(
        startDate: String,
        endDate: String,
        intervalFunction: String = "",
        patientId: Int? = null,
    ): ExerciseListDto
}