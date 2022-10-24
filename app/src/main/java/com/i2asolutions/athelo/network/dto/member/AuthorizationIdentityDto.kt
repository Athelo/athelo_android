package com.i2asolutions.athelo.network.dto.member


import com.i2asolutions.athelo.presentation.model.member.AuthorizationIdentity
import com.i2asolutions.athelo.presentation.model.member.toIdentityType
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