package com.i2asolutions.athelo.presentation.model.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ErrorResponse(val message: String) : Parcelable