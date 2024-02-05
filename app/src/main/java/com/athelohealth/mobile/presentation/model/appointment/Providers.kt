package com.athelohealth.mobile.presentation.model.appointment

import androidx.annotation.Keep

@Keep
data class Providers(
    val count: Int?,
    val providers: List<Provider>?
)
