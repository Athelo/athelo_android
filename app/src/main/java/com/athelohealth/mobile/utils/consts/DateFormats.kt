package com.athelohealth.mobile.utils.consts

internal val yyyyMMddRegex = "\\d{4}-\\d{2}-\\d{2}".toRegex()
internal val isoRegex =
    "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{6}(Z|((\\\\+|-)\\d{2}:?(\\d{2})?))".toRegex()
internal val isoRegex2 =
    "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(Z|((\\\\+|-)\\d{2}:?(\\d{2})?))".toRegex()
internal val isoRegex3 =
    "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}(Z|((\\\\+|-)\\d{2}:?(\\d{2})?))".toRegex()
internal val isoNoTimezoneRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}".toRegex()

const val DATE_FORMAT_ISO_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'"
const val DATE_FORMAT_SIMPLE_DAY = "yyyy-MM-dd"
const val DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXX"
const val DATE_FORMAT_ISO_2 = "yyyy-MM-dd'T'HH:mm:ssXX"
const val DATE_FORMAT_ISO_3 = "yyyy-MM-dd'T'HH:mm:ss.SSSXX"
const val DATE_FORMAT_CHAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
const val DATE_FORMAT_ISO_NO_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss"
const val DATE_FORMAT_DISPLAY_BIRTH_DATE = "MMMM yyyy"
const val DATE_FORMAT_INPUT_DISPLAY_BIRTH_DATE = "MMMM, yyyy"
const val DATE_FORMAT_FULL = "MMMM dd, yyyy"
const val DATE_FORMAT_SHORT = "MMM dd, yyyy"
const val DATE_FORMAT_WEEK_NAME = "EEEE"
const val DATE_FORMAT_WEEK_NAME_SHORT = "EE"