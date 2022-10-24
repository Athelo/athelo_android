package com.i2asolutions.athelo.presentation.model.sleep

import androidx.annotation.StringRes
import com.i2asolutions.athelo.R

enum class HistoryRange(val tab: Int, @StringRes val overviewText: Int) {
    Day(0, R.string.Daily_Overview),
    Week(1, R.string.Week_Overview),
    Month(2, R.string.Month_Overview)
}