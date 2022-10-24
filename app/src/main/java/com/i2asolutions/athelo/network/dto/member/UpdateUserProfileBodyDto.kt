package com.i2asolutions.athelo.network.dto.member

import com.i2asolutions.athelo.network.dto.base.RetrofitNullableStringParam
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateUserProfileBodyDto(
    @SerialName("email") private val email: String,
    @SerialName("phone_number") private val phoneNumber: RetrofitNullableStringParam?,
    @SerialName("date_of_birth") private val birthdate: String?,
    @SerialName("display_name") private val displayName: String,
    @SerialName("athelo_user_type") private val userType: String?,
)