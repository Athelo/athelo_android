package com.athelohealth.mobile.extensions

import com.athelohealth.mobile.presentation.model.sleep.HistoryRange
import com.athelohealth.mobile.utils.consts.DATE_FORMAT_SIMPLE_DAY
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun <T> SortedMap<Date, List<T>>.fillGaps(start: Date, end: Date): SortedMap<Date, List<T>> {
    val returnMap = TreeMap<Date, List<T>>()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val periodEnd = sdf.format(end)
    val calendar = Calendar.getInstance().apply { time = start }
    var current: String
    do {
        current = sdf.format(calendar.time)
        val validKey = keys.firstOrNull { sdf.format(it) == current }
        if (validKey != null) {
            returnMap[validKey] = filter { sdf.format(it.key) == current }.values.flatten()
        } else {
            returnMap[calendar.time] = emptyList()
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    } while (current != periodEnd)
    return returnMap
}

fun <T> SortedMap<Date, List<T>>.fillHourGaps(): SortedMap<Date, List<T>> {
    val returnMap = TreeMap<Date, List<T>>()
    val sdf = SimpleDateFormat("yyyy-MM-dd HH", Locale.US)
    val calendar =
        Calendar.getInstance().apply { time = keys.firstOrNull() ?: Date() }.getStartOfDay()
    var current: String
    for (i in 0..23) {
        current = sdf.format(calendar.time)
        val validKey = keys.firstOrNull { sdf.format(it) == current }
        if (validKey != null) {
            returnMap[validKey] = filter { sdf.format(it.key) == current }.values.flatten()
        } else {
            returnMap[calendar.time] = emptyList()
        }
        calendar.add(Calendar.HOUR_OF_DAY, 1)
    }
    return returnMap
}


fun Pair<Date, Date>.getRangeName(range: HistoryRange): String {
    val dayMs = TimeUnit.DAYS.toMillis(1)
    val weekMs = TimeUnit.DAYS.toMillis(7)
    val start = first
    val today = Date()
    val end = second
    return when (range) {
        HistoryRange.Day ->
            if (today.time in start.time..end.time) "Today"
            else if (today.time - dayMs in start.time..end.time) "Yesterday"
            else "${(today.time - start.time) / dayMs} days ago"
        HistoryRange.Week ->
            if (today.time in start.time..end.time) "This Week"
            else if (today.time - weekMs in start.time..end.time) "Previous Week"
            else "${(today.time - start.time) / weekMs} weeks ago"
        HistoryRange.Month ->
            when (val m = calculateMonthsBetween(start, today)) {
                0 -> "This Month"
                1 -> "Previous Month"
                else -> "$m months ago"
            }
    }
}

fun calculateMonthsBetween(d1: Date, d2: Date): Int {
    val prev = Calendar.getInstance().apply { time = if (d1 < d2) d1 else d2 }
    val next = Calendar.getInstance().apply { time = if (d1 < d2) d2 else d1 }
    var months = 0
    while (!(prev.get(Calendar.MONTH) == next.get(Calendar.MONTH)
                && prev.get(Calendar.YEAR) == next.get(Calendar.YEAR))
    ) {
        months++
        next.add(Calendar.MONTH, -1)
    }
    return months
}

fun Pair<Date, Date>.getFormattedDate(range: HistoryRange): String {
    return when (range) {
        HistoryRange.Day -> first.displayAsString("MMM d,yyyy")
        HistoryRange.Week -> if (calculateMonthsBetween(first, second) == 0)
            "${first.displayAsString("MMM d")} - ${second.displayAsString("d,yyyy")}"
        else "${first.displayAsString("MMM d")} - ${second.displayAsString("MMM d,yyyy")}"
        HistoryRange.Month -> first.displayAsString("MMMM yyyy")
    }
}

fun getDayPeriod(previous: Pair<Date, Date>? = null, diff: Int = 0): Pair<Date, Date> {
    val calendar = Calendar.getInstance()
    if (previous != null) calendar.time = previous.first
    if (diff != 0) calendar.add(Calendar.DAY_OF_YEAR, diff)
    return calendar.getStartOfDay().time to calendar.getEndOfDay().time
}

fun getLastWeekPeriod(): Pair<Date, Date> {
    val calendar = GregorianCalendar()
    val endDate = calendar.getEndOfDay().time
    calendar.add(GregorianCalendar.DAY_OF_YEAR, -6)
    val startDate = calendar.getStartOfDay().time
    return startDate to endDate
}

fun getWeekPeriod(previous: Pair<Date, Date>? = null, diff: Int = 0): Pair<Date, Date> {
    val calendar = GregorianCalendar()
    if (previous != null) calendar.time = previous.first
    val firstDayOfWeek = calendar.firstDayOfWeek
    if (diff == 0)
        while (calendar.get(GregorianCalendar.DAY_OF_WEEK) != firstDayOfWeek)
            calendar.add(GregorianCalendar.DAY_OF_YEAR, -1)
    else
        do {
            calendar.add(GregorianCalendar.DAY_OF_YEAR, diff)
        } while (calendar.get(GregorianCalendar.DAY_OF_WEEK) != firstDayOfWeek)
    val startDate = calendar.getStartOfDay().time
    calendar.add(GregorianCalendar.DAY_OF_YEAR, 6)
    val endDate = calendar.getEndOfDay().time
    return startDate to endDate
}

fun getMonthPeriod(previous: Pair<Date, Date>? = null, diff: Int = 0): Pair<Date, Date> {
    val calendar = GregorianCalendar()
    if (previous != null) calendar.time = previous.first
    calendar.set(GregorianCalendar.DAY_OF_MONTH, 1)
    calendar.add(GregorianCalendar.MONTH, diff)
    val startDate = calendar.getStartOfDay().time
    calendar.set(
        GregorianCalendar.DAY_OF_MONTH,
        calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)
    )
    val endDate = calendar.getEndOfDay().time
    return startDate to endDate
}

fun Pair<Date, Date>.canMoveForward() =
    Date().displayAsString(DATE_FORMAT_SIMPLE_DAY) >
            second.displayAsString(DATE_FORMAT_SIMPLE_DAY)

fun canMoveBackward() = true