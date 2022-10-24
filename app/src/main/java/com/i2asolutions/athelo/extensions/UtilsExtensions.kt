package com.i2asolutions.athelo.extensions

import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.enums.Enums

fun Enums.findFacebookAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Facebook" }
}

fun Enums.findGoogleAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Google" }
}

fun Enums.findAppleAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Apple" }
}