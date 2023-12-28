package com.athelohealth.mobile.presentation.model.exercise

import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.chart.ChartEntry
import java.util.*

data class ExerciseChartEntry(val date: Date, val value: Int) :
    BarChartEntry(
        values = listOf(ChartEntry(value / 60f, 0xFF68951B.toInt(), 0x00000000)),
        strokeGradient = listOf(0xFF68951B.toInt(), 0xFF68951B.toInt())
    ) {
    constructor(date: Date, entry: ExerciseEntry?) : this(date = date, value = entry?.value ?: 0)
}