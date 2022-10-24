package com.i2asolutions.athelo.presentation.model.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Tabs : Parcelable {
    Home, Sleep, Activity, Community, News
}