package com.i2asolutions.athelo.presentation.model.application


import kotlinx.serialization.Serializable

@Serializable
data class Application(
    val aboutUs: String,
    val privacyPolicy: String,
    val termsOfUse: String
)