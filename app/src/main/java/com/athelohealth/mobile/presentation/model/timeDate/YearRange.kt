package com.athelohealth.mobile.presentation.model.timeDate

typealias YearRange = Array<Year>

fun createYearRange(startYear: Year, size: Int = 16): YearRange {
    return Array(size) {
        Year(startYear.yearValue + it)
    }
}

fun createYearRangeUntil(lastYear: Year, size: Int = 16): YearRange {
    return Array(size) {
        Year(lastYear.yearValue + it - size)
    }
}