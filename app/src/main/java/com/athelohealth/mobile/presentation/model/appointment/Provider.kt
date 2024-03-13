package com.athelohealth.mobile.presentation.model.appointment

import androidx.annotation.Keep

@Keep
data class Provider(
    val displayName: String?,
    val id: Int?,
    val photo: String?,
    val providerType: String?
)
