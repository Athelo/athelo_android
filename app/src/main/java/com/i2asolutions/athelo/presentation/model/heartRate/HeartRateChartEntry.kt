package com.i2asolutions.athelo.presentation.model.heartRate

import com.i2asolutions.athelo.presentation.model.chart.LineChartEntry
import java.util.*

data class HeartRateChartEntry(val date: Date, val steps: Int) :
    LineChartEntry(
        value = steps.toFloat(),
        bgColor = 0xFF68951B.toInt()
    ) {
    constructor(date: Date, entry: HeartRateEntry?) : this(date = date, steps = entry?.value ?: 0)
}