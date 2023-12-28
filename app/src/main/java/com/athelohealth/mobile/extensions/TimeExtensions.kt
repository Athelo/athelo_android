package com.athelohealth.mobile.extensions

import com.athelohealth.mobile.presentation.model.timeDate.BirthDate
import com.athelohealth.mobile.presentation.model.timeDate.Year
import com.athelohealth.mobile.presentation.model.timeDate.parse
import com.athelohealth.mobile.utils.DateTimeFormat
import com.athelohealth.mobile.utils.consts.*
import com.athelohealth.mobile.utils.consts.isoRegex
import com.athelohealth.mobile.utils.consts.yyyyMMddRegex
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Date?.displayAsString(
    format: String,
    locale: Locale = Locale.ENGLISH,
    timeZone: TimeZone = TimeZone.getDefault()
) = this?.let {
    runCatching {
        SimpleDateFormat(format, locale).apply { setTimeZone(timeZone) }.format(this)
    }.onFailure { debugPrint(it) }.getOrDefault("")
} ?: ""

fun String?.toDate(
    locale: Locale = Locale.ENGLISH,
): Date? {
    if (this == null) return null
    return when {
        this.matches(yyyyMMddRegex) -> toDate(DATE_FORMAT_SIMPLE_DAY, locale)
        this.matches(isoRegex) -> toDate(DATE_FORMAT_ISO, locale)
        this.matches(isoRegex2) -> toDate(DATE_FORMAT_ISO_2, locale)
        this.matches(isoRegex3) -> toDate(DATE_FORMAT_ISO_3, locale)
        this.matches(isoNoTimezoneRegex) -> toDate(DATE_FORMAT_ISO_NO_TIMEZONE, locale)
        else -> null
    }
}

fun String?.toDate(
    format: String,
    locale: Locale = Locale.ENGLISH,
    timeZone: TimeZone = TimeZone.getDefault()
) = this?.let {
    runCatching {
        SimpleDateFormat(format, locale).apply { setTimeZone(timeZone) }.parse(this)
    }.onFailure { debugPrint(it) }.getOrNull()
}

fun String?.displayAsDifferentDateFormat(expectedFormat: String, locale: Locale = Locale.ENGLISH) =
    toDate(locale).displayAsString(expectedFormat, locale)

fun String?.toBirthDate(): BirthDate? {
    val date = toDate(DATE_FORMAT_SIMPLE_DAY) ?: return null
    val calendar = Calendar.getInstance().apply { time = date }
    return BirthDate(Year(calendar[Calendar.YEAR]), parse(calendar[Calendar.MONTH] + 1))
}

/**
 * returns timestamp (milliseconds) between two dates
 *
 * @param   anotherDate   date to calculation difference
 */
fun Date.timestampTo(anotherDate: Date): Long {
    val ts1 = time
    val ts2 = anotherDate.time

    return abs(ts1 - ts2)
}

/**
 * returns <code>DateTimeFormat</code> for the given timestamp
 */
fun Long.toDateTimeFormat(): DateTimeFormat {
    val days = TimeUnit.MILLISECONDS.toDays(this)
    return when {
        days == 0L -> DateTimeFormat.TheSameDay
        days < 7 -> DateTimeFormat.TheSameWeek
        else -> DateTimeFormat.Other
    }
}

fun String.nanosecondsToDate(): Date {
    return (this.toLongOrNull() ?: 0L).nanosecondsToDate()
}

fun Long.nanosecondsToDate(): Date {
    return Date(TimeUnit.MILLISECONDS.convert(this, TimeUnit.NANOSECONDS))
}

fun Date.toNanoseconds(): Long {
    return TimeUnit.NANOSECONDS.convert(this.time, TimeUnit.MILLISECONDS)
}

fun Date?.displayAsChatDate() = this?.let {
    displayAsString("h:mm a")
} ?: ""

fun Date?.displayAsChatSeparator() = this?.let {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    val today = sdf.format(Date())
    val thisDate = Calendar.getInstance().apply { time = it }
    when (today) {
        sdf.format(thisDate.time) -> {
            "Today"
        }
        sdf.format(thisDate.apply { add(Calendar.DAY_OF_YEAR, 1) }.time) -> {
            "Yesterday"
        }
        else -> {
            displayAsString("MMMM d, yyyy")
        }
    }
} ?: ""

fun Int?.displaySecsAsTime(fallback: String = "") = this?.let {
    if (it == 0) fallback
    else {
//    else if (it >= 60 * 60) {
        //show hours minutes
        val hours = it / (60 * 60)
        val minutes = (it - (hours * 60 * 60)) / 60
        "${hours}h ${minutes}m"
    }
//    } else {
//        //show minutes seconds
//        val minutes = it / 60
//        val secs = (it - (minutes * 60))
//        "${minutes}m ${secs}s"
//    }
} ?: fallback

fun Calendar.getStartOfDay(): Calendar {
    return Calendar.getInstance().apply {
        time = this@getStartOfDay.time
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Calendar.getEndOfDay(): Calendar {
    return Calendar.getInstance().apply {
        time = this@getEndOfDay.time
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
}