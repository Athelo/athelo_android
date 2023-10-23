package com.i2asolutions.athelo.presentation.model.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Enums(
    var userTypes: List<EnumItem> = emptyList(),
    var inappropriateContentReason: List<EnumItem> = emptyList(),
    var reportedChatMessageType: List<EnumItem> = emptyList(),
    var feedbackCategory: List<EnumItem> = emptyList(),
    var thirdPartyAuthorizationType: List<EnumItem> = emptyList(),
    var caregiverRelations: List<EnumItem> = emptyList(),
) : Parcelable