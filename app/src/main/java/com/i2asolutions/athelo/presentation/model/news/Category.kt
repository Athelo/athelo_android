package com.i2asolutions.athelo.presentation.model.news

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val id: Int, val name: String) : Parcelable