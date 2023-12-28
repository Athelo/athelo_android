package com.athelohealth.mobile.network.dto.application


import com.athelohealth.mobile.presentation.model.application.Application
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("identifier") val identifier: String? = null,
    @SerialName("about_us") val aboutUs: String? = null,
    @SerialName("privacy") val privacyPolicy: String? = null,
    @SerialName("terms_of_use") val termsOfUse: String? = null
) {
    fun toApplication(): Application =
        Application(aboutUs ?: "", privacyPolicy ?: "", termsOfUse ?: "")
}