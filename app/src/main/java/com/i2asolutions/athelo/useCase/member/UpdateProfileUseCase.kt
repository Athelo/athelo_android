package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.extensions.ifEmptyNull
import com.i2asolutions.athelo.network.repository.member.MemberRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: MemberRepository) {

    suspend operator fun invoke(
        userId: Int,
        phoneNumber: String,
        birthDate: String?,
        email: String,
        displayName: String,
        userType: String,
    ) = repository.updateUserProfile(
        userId = userId,
        phoneNumber = phoneNumber.ifEmptyNull(),
        birthDate = birthDate.ifEmptyNull(),
        email = email,
        displayName = displayName,
        userType = userType.ifEmptyNull()
    )
}