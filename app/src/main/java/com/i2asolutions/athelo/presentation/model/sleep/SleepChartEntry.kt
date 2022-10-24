package com.i2asolutions.athelo.presentation.model.sleep

import com.i2asolutions.athelo.presentation.model.chart.BarChartEntry
import com.i2asolutions.athelo.presentation.model.chart.ChartEntry
import java.util.*

class SleepChartEntry(val date: Date, deepSleepSec: Int, remSec: Int, lightSec: Int) :
    BarChartEntry(
        values = listOf(
            ChartEntry(deepSleepSec / (60f * 60f), 0xFFDEEEC3.toInt(), 0xFF000000.toInt()),
            ChartEntry(remSec / (60f * 60f), 0xFF68951B.toInt(), 0xFFFFFFFF.toInt()),
            ChartEntry(lightSec / (60f * 60f), 0xFF7FC704.toInt(), 0xFFFFFFFF.toInt()),
        ),
        strokeGradient = listOf(0xFF2B4302.toInt(), 0xFF7B9157.toInt())
    ) {
    constructor(date: Date, entry: List<SleepEntry>) : this(
        date = date,
        deepSleepSec = entry.firstOrNull { it.level == SleepLevel.Deep }?.duration ?: 0,
        remSec = entry.firstOrNull { it.level == SleepLevel.Rem }?.duration ?: 0,
        lightSec = entry.firstOrNull { it.level == SleepLevel.Light }?.duration ?: 0,
    )
}