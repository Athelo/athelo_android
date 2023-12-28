package com.athelohealth.mobile.useCase.common

import com.athelohealth.mobile.network.repository.common.CommonRepository
import com.athelohealth.mobile.presentation.model.enums.Enums
import javax.inject.Inject

class LoadEnumsUseCase @Inject constructor(
    private val repository: CommonRepository,
    val enums: Enums
) {

    suspend operator fun invoke(): Enums = repository.getEnums().toEnums().also {
        enums.userTypes = it.userTypes
        enums.thirdPartyAuthorizationType = it.thirdPartyAuthorizationType
        enums.inappropriateContentReason = it.inappropriateContentReason
        enums.reportedChatMessageType = it.reportedChatMessageType
        enums.feedbackCategory = it.feedbackCategory
        enums.caregiverRelations = it.caregiverRelations
    }
}