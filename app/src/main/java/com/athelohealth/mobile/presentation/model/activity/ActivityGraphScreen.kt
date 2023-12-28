package com.athelohealth.mobile.presentation.model.activity

import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.LineChartDataSet

sealed interface ActivityGraphScreen {
    class ActivityPeriodInfo(
        val date: String,
        val rangeName: String,
        val canGoBack: Boolean,
        val canGoForward: Boolean
    )

    class ActivityInformation(
        val dataSet: BarChartDataSet,
        val total: String
    )

    class ActivityLineInformation(
        val dataSet: LineChartDataSet,
        val total: String
    )
}