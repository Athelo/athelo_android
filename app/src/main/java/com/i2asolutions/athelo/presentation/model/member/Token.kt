package com.i2asolutions.athelo.presentation.model.member

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Token(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val scope: String,
    val expiresIn: Int = 0
) : Parcelable