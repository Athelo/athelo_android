package com.athelohealth.mobile.presentation.model.chart

import androidx.annotation.ColorInt
import androidx.compose.ui.geometry.Rect

open class LineChartEntry(
    value: Float,
    @ColorInt bgColor: Int,
    var clickRect: Rect = Rect(0f, 0f, 0f, 0f)
) : ChartEntry(value, bgColor, 0x00000000)