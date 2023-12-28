package com.athelohealth.mobile.presentation.model.chart

import androidx.annotation.ColorInt

class LineChartDataSet(
    val values: List<LineChartEntry>,
    val yAxisFormatter: (Float) -> String = { it.toString() },
    val xAxisFormatter: (LineChartEntry) -> String = { it.toString() },
    val cloudFormatter: (LineChartEntry) -> Pair<String, String> = { "" to "" },
    val xAxisMaxValue: Int = 31,
    val xAxisMinLabel: Int = 1,
    val xAxisMaxLabel: Int = 30,
    val xLabelStep: Int = 5,
    @ColorInt val lineColor: Int,
    val backgroundGradient: List<Int>,
    val hourMode: Boolean = false,
    val drawYLines: Boolean = false,
    val yLabelStep: Int = 1,
    val customMaxValue: Float? = null
) {
    fun getMaxYValue() = customMaxValue ?: values.maxOfOrNull { it.value }
}