package com.i2asolutions.athelo.presentation.model.home

import androidx.annotation.DrawableRes
import com.i2asolutions.athelo.R

enum class Feelings(val feelingName: String, @DrawableRes val icon: Int) {
    Sad("Sad", R.drawable.face_sad),
    Ok("OK", R.drawable.face_ill),
    Good("Good", R.drawable.face_smile),
}