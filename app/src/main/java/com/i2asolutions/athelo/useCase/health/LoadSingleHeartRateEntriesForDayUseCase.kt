package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadSingleHeartRateEntriesForDayUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(day: Date, patientId: Int?): List<HeartRateEntry> {
        val results = arrayListOf<HeartRateEntry>()
        var url: String? = null
        do {
            val result = if (url == null) repository.getHeartRateSingleRecordsForDay(
                date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(day), patientId = patientId
            ) else repository.getHeartRateSingleRecordsForUrl(url)

            url = result.nextUrl
            results.addAll(result.results.map { it.toHeartRateEntry() })
        } while (url != null)

        return results
    }
}