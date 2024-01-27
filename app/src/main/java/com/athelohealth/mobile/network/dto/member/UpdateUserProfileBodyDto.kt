package com.athelohealth.mobile.network.dto.member

import com.athelohealth.mobile.network.dto.base.RetrofitNullableStringParam
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateUserProfileBodyDto(
    @SerialName("email") private val email: String,
    @SerialName("phone") private val phoneNumber: RetrofitNullableStringParam?,
    @SerialName("birthday") private val birthdate: String?,
    @SerialName("display_name") private val displayName: String,

)