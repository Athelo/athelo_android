package com.i2asolutions.athelo.presentation.model.member

import android.os.Parcelable
import com.i2asolutions.athelo.extensions.displayAsDifferentDateFormat
import com.i2asolutions.athelo.presentation.model.base.Image
import com.i2asolutions.athelo.utils.consts.DATE_FORMAT_DISPLAY_BIRTH_DATE
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class User(
    val displayName: String? = null,
    val email: String? = null,
    val birthday: String? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val id: Int? = null,
    val isFriend: Boolean? = null,
    val photo: Image? = null,
    val userType: Int? = null,
    val userId: String? = null,
    val status: UserStatus? = null,
    val profileId: Int? = null,
    val privateProfile: Boolean? = null,
    val fitBitConnected: Boolean = false
) : Parcelable {


    @IgnoredOnParcel
    val formattedDisplayName: String = when {
        !lastName.isNullOrBlank() && !firstName.isNullOrBlank() ->
            "%s %s".format(firstName, lastName)
        !lastName.isNullOrBlank() -> lastName
        !firstName.isNullOrBlank() -> firstName
        displayName != null && displayName.isNotBlank() -> displayName
        else -> ""
    }

    @IgnoredOnParcel
    val formattedBirthdate: String?
        get() = birthday.displayAsDifferentDateFormat(DATE_FORMAT_DISPLAY_BIRTH_DATE)
            .let { if (it.isNullOrBlank()) null else it }
}