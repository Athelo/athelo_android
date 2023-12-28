package com.athelohealth.mobile.presentation.model.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ErrorResponse(val message: String) : Parcelable