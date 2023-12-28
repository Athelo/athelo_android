package com.athelohealth.mobile.presentation.model.sleep

import androidx.compose.ui.graphics.Color
import com.athelohealth.mobile.presentation.model.chart.CircleChartDataSet
import java.text.SimpleDateFormat
import java.util.*

class SleepCircleChartEntry : CircleChartDataSet(
    value = 0.75f,
    bgColor = Color(0x5468951B),
    textColor = Color(0xFF68951B.toInt()),
    valueColor = Color(0xFF68951B.toInt()),
    formattedValue = SimpleDateFormat(
        "h'h' mm'm'",
        Locale.US
    ).format(12 * 60 * 60 * 1000 * 0.75f)

) {
    companion object {
        val MOCK = SleepCircleChartEntry()
    }
}