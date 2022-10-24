package com.i2asolutions.athelo.presentation.model.member


enum class IdentityType(val value: String) {
    Native("I2A_IDENTITY"), Social("SOCIAL_IDENTITY"), Unknown("Unknown")
}

fun String?.toIdentityType(): IdentityType =
    when (this) {
        IdentityType.Native.value -> IdentityType.Native
        IdentityType.Social.value -> IdentityType.Social
        else -> IdentityType.Unknown
    }