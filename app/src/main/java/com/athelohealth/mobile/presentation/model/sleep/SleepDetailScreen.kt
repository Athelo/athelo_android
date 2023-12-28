package com.athelohealth.mobile.presentation.model.sleep

import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet

sealed interface SleepDetailScreen {
    class PeriodInfo(
        val date: String,
        val rangeName: String,
        val canGoBack: Boolean,
        val canGoForward: Boolean
    )

    class DailyInformation(
        val deepSleep: String,
        val rem: String,
        val lightSleep: String,
        val awake: String,
        val total: String
    )

    class WeeklyInformation(
        val sleepDataSet: BarChartDataSet,
        val avgSleepValue: String,
    )

    class MonthlyInformation(
        val sleepDataSet: BarChartDataSet,
        val avgSleepValue: String,
        val deepSleepPercentage: String,
        val remSleepPercentage: String,
        val lightSleepPercentage: String,
    )
}