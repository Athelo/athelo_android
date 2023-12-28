package com.athelohealth.mobile.presentation.model.calendar

import java.util.*

typealias DayRange = Array<Day>

fun createDayRange(startDate: Calendar, size: Int = 7): DayRange = Array(size) {
    startDate.toDay().also {
        startDate.add(Calendar.DAY_OF_YEAR, 1)
    }

}