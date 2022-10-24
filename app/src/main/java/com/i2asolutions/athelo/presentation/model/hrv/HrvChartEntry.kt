package com.i2asolutions.athelo.presentation.model.hrv

import com.i2asolutions.athelo.presentation.model.chart.LineChartEntry
import java.util.*

data class HrvChartEntry(val date: Date, val steps: Int) :
    LineChartEntry(
        value = steps.toFloat(),
        bgColor = 0xFF68951B.toInt()
    ) {
    constructor(date: Date, entry: HrvEntry?) : this(date = date, steps = entry?.value ?: 0)
}