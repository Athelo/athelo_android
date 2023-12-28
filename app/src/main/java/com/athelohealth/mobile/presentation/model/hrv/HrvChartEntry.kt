package com.athelohealth.mobile.presentation.model.hrv

import com.athelohealth.mobile.presentation.model.chart.LineChartEntry
import java.util.*

data class HrvChartEntry(val date: Date, val steps: Int) :
    LineChartEntry(
        value = steps.toFloat(),
        bgColor = 0xFF68951B.toInt()
    ) {
    constructor(date: Date, entry: HrvEntry?) : this(date = date, steps = entry?.value ?: 0)
}

data class EmptyHrvChartEntry(val date: Date) : LineChartEntry(0f, 0)