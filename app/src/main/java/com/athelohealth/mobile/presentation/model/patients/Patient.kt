package com.athelohealth.mobile.presentation.model.patients

import com.athelohealth.mobile.presentation.model.base.Image

data class Patient(
    val userId: String = "",
    val name: String = "",
    val relation: String = "",
    val image: Image? = null,
    var selected: Boolean = false
)
