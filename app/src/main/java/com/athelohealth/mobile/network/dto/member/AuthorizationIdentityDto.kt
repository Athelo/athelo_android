package com.athelohealth.mobile.network.dto.member


import com.athelohealth.mobile.presentation.model.member.AuthorizationIdentity
import com.athelohealth.mobile.presentation.model.member.toIdentityType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationIdentityDto(
    @SerialName("email")
    val email: String? = null,
    @SerialName("login_type")
    val loginType: String? = null,
) {
    fun toAuthorizationIdentity(): AuthorizationIdentity {
        return AuthorizationIdentity(loginType.toIdentityType())
    }
}