package com.athelohealth.mobile.presentation.model.home

import androidx.annotation.DrawableRes
import com.athelohealth.mobile.R

enum class Feelings(val feelingName: String, @DrawableRes val icon: Int) {
    Sad("Sad", R.drawable.face_sad),
    Ok("OK", R.drawable.face_ill),
    Good("Good", R.drawable.face_smile),
}