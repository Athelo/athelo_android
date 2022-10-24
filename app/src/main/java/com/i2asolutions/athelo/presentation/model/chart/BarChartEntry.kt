package com.i2asolutions.athelo.presentation.model.chart

import androidx.compose.ui.geometry.Rect

open class BarChartEntry(
    val values: List<ChartEntry>,
    val strokeGradient: List<Int>,
    var clickRect: Rect = Rect(0f, 0f, 0f, 0f)
) {
    val total = values.sumOf { it.value.toDouble() }.toFloat()
}