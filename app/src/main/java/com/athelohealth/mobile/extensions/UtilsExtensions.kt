package com.athelohealth.mobile.extensions

import com.athelohealth.mobile.presentation.model.enums.EnumItem
import com.athelohealth.mobile.presentation.model.enums.Enums

fun Enums.findFacebookAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Facebook" }
}

fun Enums.findGoogleAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Google" }
}

fun Enums.findAppleAuthorizationEnum(): EnumItem? {
    return thirdPartyAuthorizationType.find { it.label == "Apple" }
}