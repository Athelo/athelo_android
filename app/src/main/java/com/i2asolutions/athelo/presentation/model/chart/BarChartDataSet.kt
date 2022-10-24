package com.i2asolutions.athelo.presentation.model.chart

open class BarChartDataSet(
    val values: List<BarChartEntry>,
    val yAxisFormatter: (Float) -> String = { it.toString() },
    val xAxisFormatter: (BarChartEntry) -> String = { it.toString() },
    val valueFormatter: (ChartEntry) -> String = { it.toString() },
    val cloudFormatter: (BarChartEntry) -> Pair<String, String> = { "" to "" },
    val xLabelStep: Int = 1,
    val xAxisMinLabel: Int = 0,
    val xAxisMaxLabel: Int = 30,
    val barRatio: Float = 0.6f,
    val yLabelStep: Int = 1,
    val drawYLines: Boolean = false,
    val customMaxValue: Float? = null,
    val hourMode: Boolean = false,
) {
    fun getMaxYValue() = customMaxValue ?: values.maxOfOrNull { it.total }
}