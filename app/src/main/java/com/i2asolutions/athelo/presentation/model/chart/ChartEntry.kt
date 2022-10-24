package com.i2asolutions.athelo.presentation.model.chart

import androidx.annotation.ColorInt

open class ChartEntry(
    val value: Float,
    @ColorInt val bgColor: Int,
    @ColorInt val textColor: Int
)

object EmptyChartEntry : LineChartEntry(0f, 0)