package com.i2asolutions.athelo.network.dto.member

import com.i2asolutions.athelo.network.dto.base.ImageDto
import com.i2asolutions.athelo.presentation.model.chat.SimpleUser
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.member.toUserStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDto(
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("is_friend") val isFriend: Boolean? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("photo") val photo: ImageDto? = null,
    @SerialName("status") val status: Int? = null,
    @SerialName("athelo_user_type") val userType: Int? = null,
    @SerialName("user") val user: SocialAppUserDto? = null,
    @SerialName("user_profile_id") val userProfileId: Int? = null,
    @SerialName("profile_friend_visibility_only") val profileFriendVisibleOnly: Boolean? = null,
    @SerialName("date_of_birth") val birthdate: String? = null, // format 1991-06-15
    @SerialName("phone_number") val phoneNumber: String? = null, // format 1991-06-15
    @SerialName("has_fitbit_user_profile") val hasFitbitUserProfile: Boolean? = null,
    @SerialName("is_caregiver") val isCaregiver: Boolean? = null,
    @SerialName("is_patient") val isPatient: Boolean? = null,
) {
    val fullName: String
        get() = when {
            !firstName.isNullOrBlank() && !lastName.isNullOrBlank() -> "$firstName $lastName"
            !lastName.isNullOrBlank() -> lastName
            !firstName.isNullOrBlank() -> firstName
            !displayName.isNullOrBlank() -> displayName
            else -> ""
        }

    fun toUser(): User {
        return User(
            displayName = displayName,
            email = email,
            firstName = firstName,
            id = id,
            isFriend = isFriend,
            lastName = lastName,
            photo = photo?.toImage(),
            userId = user?.username,
            status = status.toUserStatus(),
            profileId = userProfileId,
            privateProfile = profileFriendVisibleOnly,
            birthday = birthdate,
            phone = phoneNumber,
            userType = userType,
            fitBitConnected = hasFitbitUserProfile ?: false,
            isCaregiver = isCaregiver ?: false,
        )
    }

    fun toSimpleUser(): SimpleUser {
        return SimpleUser(photo = photo?.toImage(), displayName = fullName, id = id ?: -1)
    }
}