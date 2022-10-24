package com.i2asolutions.athelo.presentation.model.sleep

import com.i2asolutions.athelo.presentation.model.chart.*
import java.util.*

class SleepMonthChartEntry(val date: Date, sleepSec: Int) : BarChartEntry(
    values = listOf(ChartEntry(sleepSec / (60f * 60f), 0xFF68951B.toInt(), 0x00000000)),
    strokeGradient = listOf(0xFF68951B.toInt(), 0xFF68951B.toInt())
)