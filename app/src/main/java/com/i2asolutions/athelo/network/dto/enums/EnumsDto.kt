package com.i2asolutions.athelo.network.dto.enums

import com.i2asolutions.athelo.presentation.model.enums.Enums
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class EnumsDto(
    @SerialName("inappropriate_content_reason") val inappropriateContentReason: List<EnumItemDto>,
    @SerialName("reported_chat_message_type") val reportedChatMessageType: List<EnumItemDto>,
    @SerialName("feedback_category") val feedbackCategory: List<EnumItemDto>,
    @SerialName("third_party_access_token_source") val accessThirdTokens: List<EnumItemDto>,
    @SerialName("caregiver_relation_label") val caregiverRelationLabel: List<EnumItemDto>,
) {

    fun toEnums(): Enums = Enums(
        userTypes = emptyList(),
        inappropriateContentReason = inappropriateContentReason.map { it.toEnumItem() },
        reportedChatMessageType = reportedChatMessageType.map { it.toEnumItem() },
        feedbackCategory = feedbackCategory.map { it.toEnumItem() },
        thirdPartyAuthorizationType = accessThirdTokens.map { it.toEnumItem() },
        caregiverRelations = caregiverRelationLabel.map { it.toEnumItem() }
    )
}