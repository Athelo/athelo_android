package com.i2asolutions.athelo.useCase.health

import com.i2asolutions.athelo.network.repository.health.HealthRepository
import com.i2asolutions.athelo.presentation.model.heartRate.HeartRateEntry
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LoadSingleHeartRateEntriesForDayUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(day: Date): List<HeartRateEntry> {
        val results = arrayListOf<HeartRateEntry>()
        var url: String? = null
        do {
            val result = if (url == null) repository.getHeartRateSingleRecordsForDay(
                SimpleDateFormat("yyyy-MM-dd", Locale.US).format(day)
            ) else repository.getHeartRateSingleRecordsForUrl(url)

            url = result.nextUrl
            results.addAll(result.results.map { it.toHeartRateEntry() })
        } while (url != null)

        return results
    }
}