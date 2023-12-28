package com.athelohealth.mobile.network.dto.base

import android.os.Parcelable
import com.athelohealth.mobile.presentation.model.base.Image
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class ImageDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("image_100_100") val image100100: String? = null,
    @SerialName("image_125_125") val image125125: String? = null,
    @SerialName("image_250_250") val image250250: String? = null,
    @SerialName("image_500_500") val image500500: String? = null,
    @SerialName("image_50_50") val image5050: String? = null,
    @SerialName("name") val name: String? = null
) : Parcelable {
    fun toImage(): Image {
        return Image(image, image100100, image125125, image250250, image500500, image5050)
    }
}