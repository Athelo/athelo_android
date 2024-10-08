package com.i2asolutions.athelo.presentation.model.step

import com.i2asolutions.athelo.presentation.model.chart.BarChartEntry
import com.i2asolutions.athelo.presentation.model.chart.ChartEntry
import java.util.*

data class StepChartEntry(val date: Date, val steps: Int) :
    BarChartEntry(
        values = listOf(ChartEntry(steps.toFloat(), 0xFF68951B.toInt(), 0x00000000)),
        strokeGradient = listOf(0xFF68951B.toInt(), 0xFF68951B.toInt())
    ) {
    constructor(date: Date, entry: StepEntry?) : this(date = date, steps = entry?.value ?: 0)
}