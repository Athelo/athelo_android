package com.athelohealth.mobile.utils.app

sealed interface AppType {
    object Unknown : AppType
    object Patient : AppType
    data class Caregiver(var currentPatientId: String) : AppType

}

val AppType.type: Int
    get() = when (this) {
        AppType.Patient -> 0
        is AppType.Caregiver -> 1
        else -> -1
    }

val AppType.patientId: String?
    get() = when (this) {
        is AppType.Caregiver -> currentPatientId
        else -> null
    }

fun Int.toAppType(currentViewCustomer: String): AppType = when (this) {
    0 -> AppType.Patient
    1 -> AppType.Caregiver(currentViewCustomer)
    else -> AppType.Unknown
}