package com.athelohealth.mobile.network.dto.appointment


import kotlinx.serialization.Serializable

@Serializable
data class ResultDto(
  //  @SerializedName("display_name")
    val display_name: String?,
    val id: Int?,
    val photo: String?,
   // @SerializedName("provider_type")
    val provider_type: String?
)