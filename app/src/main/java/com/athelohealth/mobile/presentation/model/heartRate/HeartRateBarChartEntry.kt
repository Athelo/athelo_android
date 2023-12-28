package com.athelohealth.mobile.presentation.model.heartRate

import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.chart.ChartEntry
import java.util.*

data class HeartRateBarChartEntry(val date: Date, val data: List<ChartEntry>) :
    BarChartEntry(values = data, strokeGradient = emptyList()) {
    constructor(entries: List<HeartRateEntry>, date: Date) : this(date = date, data = entries.map {
        ChartEntry(it.value.toFloat(), 0xFF68951B.toInt(), 0x00000000)
    })
}