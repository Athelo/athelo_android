package com.athelohealth.mobile.presentation.model.sleep

import androidx.annotation.StringRes
import com.athelohealth.mobile.R

enum class HistoryRange(val tab: Int, @StringRes val overviewText: Int) {
    Day(0, R.string.Daily_Overview),
    Week(1, R.string.Week_Overview),
    Month(2, R.string.Month_Overview)
}