package com.athelohealth.mobile.presentation.model.calendar

import android.os.Parcelable
import androidx.annotation.IntRange
import com.athelohealth.mobile.extensions.displayAsString
import com.athelohealth.mobile.presentation.model.timeDate.Month
import com.athelohealth.mobile.presentation.model.timeDate.parse
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_WEEK_NAME
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_WEEK_NAME_SHORT
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Day(
    @IntRange(from = 1, to = 31) val day: Int,
    val month: Month,
    val year: Int,
    val weekName: String,
    val shortWeekName: String,
    val isToday: Boolean
) : Parcelable {
    val fullName: String
        get() = "${month.longName} $day, $year"

    operator fun compareTo(other: Day): Int {
        return when {
            year > other.year -> 1
            year < other.year -> -1
            month.value > other.month.value -> 1
            month.value < other.month.value -> -1
            day > other.day -> 1
            day < other.day -> -1
            else -> 0
        }
    }
}

fun Calendar.toDay(): Day {
    val day = this[Calendar.DAY_OF_MONTH]
    val month = parse(this[Calendar.MONTH] + 1)
    val year = this[Calendar.YEAR]
    val weekName = this.time.displayAsString(DATE_FORMAT_WEEK_NAME)
    val sWeekName = this.time.displayAsString(DATE_FORMAT_WEEK_NAME_SHORT).substring(0..1)
    val isToday = Calendar.getInstance()
        .let { todayCalendar -> todayCalendar[Calendar.YEAR] == year && todayCalendar[Calendar.DAY_OF_YEAR] == this[Calendar.DAY_OF_YEAR] }
    return Day(day, month, year, weekName, sWeekName, isToday)
}

fun Date.toDay(): Day {
    return Calendar.getInstance().also { it.time = this }.toDay()
}
