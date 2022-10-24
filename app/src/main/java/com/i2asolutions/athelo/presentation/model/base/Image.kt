package com.i2asolutions.athelo.presentation.model.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class Image(
    val image: String? = null,
    val image100100: String? = null,
    val image125125: String? = null,
    val image250250: String? = null,
    val image500500: String? = null,
    val image5050: String? = null,
) : Parcelable {
    fun get100orBigger(): String? =
        arrayOf(image100100, image125125, image250250, image500500)
            .firstOrNull { !it.isNullOrBlank() }
}