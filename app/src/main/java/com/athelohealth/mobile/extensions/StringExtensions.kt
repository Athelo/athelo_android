package com.athelohealth.mobile.extensions

import android.telephony.PhoneNumberUtils
import java.util.*

fun String?.ifEmptyNull(): String? {
    return if (isNullOrBlank()) null else this
}

fun CharSequence.phoneFilter(): CharSequence =
    this.filterIndexed { index, it -> it.isDigit() || ('+' == it && index == 0) || '-' == it }

fun String.phoneFilter(): String =
    this.filterIndexed { index, it -> it.isDigit() || ('+' == it && index == 0) || it == '-' }

fun String.phoneMask(countryCode: String = Locale.US.country): String =
    PhoneNumberUtils.formatNumber(
        this.padEnd(if (this.startsWith("+")) 12 else 10, ' '),
        countryCode
    ) ?: this