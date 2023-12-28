package com.athelohealth.mobile.presentation.model.chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

open class CircleChartDataSet(
    value: Float,
    bgColor: Color,
    textColor: Color,
    val valueColor: Color,
    val formattedValue: String
) : ChartEntry(value, bgColor.toArgb(), textColor.toArgb()) {

}