package com.i2asolutions.athelo.presentation.model.patients

import com.i2asolutions.athelo.presentation.model.base.Image

data class Patient(
    val userId: String = "",
    val name: String = "",
    val relation: String = "",
    val image: Image? = null,
    var selected: Boolean = false
)
