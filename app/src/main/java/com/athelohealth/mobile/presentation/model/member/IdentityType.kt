package com.athelohealth.mobile.presentation.model.member

private const val FIREBASE = "firebase"
enum class IdentityType(val value: String) {
    Native("NATIVE_IDENTITY"), Social("SOCIAL_IDENTITY"), Unknown("Unknown")
}

fun String?.toIdentityType(): IdentityType =
    this?.let {
        when(it) {
            FIREBASE -> IdentityType.Native
            else -> IdentityType.Social
        }
    } ?: IdentityType.Unknown