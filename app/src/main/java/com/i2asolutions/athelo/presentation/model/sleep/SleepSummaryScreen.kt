package com.i2asolutions.athelo.presentation.model.sleep

import com.i2asolutions.athelo.presentation.model.chart.CircleChartDataSet

sealed interface SleepSummaryScreen {
    class IdealSleep(val time: String, val articleId: Int, val title: String, val image: Int)
    class SleepResult(val text: String, val chartDataSet: CircleChartDataSet)
    class SleepInformation(
        val deepSleep: String,
        val rem: String,
        val lightSleep: String,
        val awake: String
    )
}