package com.athelohealth.mobile.presentation.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val id: String, val name: String) : Parcelable