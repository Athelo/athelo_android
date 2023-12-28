package com.athelohealth.mobile.presentation.model.member

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class AuthorizeResult(
    var firstTimeLogin: Boolean = false,
    @SerialName("token_data") var tokenData: Token
) : Parcelable