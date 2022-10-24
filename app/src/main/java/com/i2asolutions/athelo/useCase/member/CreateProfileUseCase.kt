package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.User
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(email: String, displayName: String, userTypeId: String): User =
        repository.createMyProfile(email, displayName, userTypeId).toUser()
}