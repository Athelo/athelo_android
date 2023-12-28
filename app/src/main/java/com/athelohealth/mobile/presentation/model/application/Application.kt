package com.athelohealth.mobile.presentation.model.application


import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val aboutUs: String,
    val privacyPolicy: String,
    val termsOfUse: String
)