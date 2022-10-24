package com.i2asolutions.athelo.presentation.model.member

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorizationIdentity(val type: IdentityType) : Parcelable
