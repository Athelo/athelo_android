package com.athelohealth.mobile.presentation.model.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(val type: MessageType, val message: String) : Parcelable
