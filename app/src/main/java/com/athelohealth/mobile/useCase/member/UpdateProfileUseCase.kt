package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.extensions.ifEmptyNull
import com.athelohealth.mobile.network.repository.member.MemberRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: MemberRepository) {

    suspend operator fun invoke(
        userId: Int,
        phoneNumber: String,
        birthDate: String?,
        email: String,
        displayName: String,
        userType: String,
        cancerStatus: com.athelohealth.mobile.network.dto.member.CancerStatus?,
    ) {
        repository.updateUserProfile(
            userId = userId,
            phoneNumber = phoneNumber.ifEmptyNull(),
            birthDate = birthDate.ifEmptyNull(),
            email = email,
            displayName = displayName,
            userType = userType.ifEmptyNull()
        )
        cancerStatus?.let {
            repository.updateCancerStatus(cancerStatus = cancerStatus)
        }
    }
}