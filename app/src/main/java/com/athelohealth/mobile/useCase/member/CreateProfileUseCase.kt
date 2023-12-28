package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.User
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(email: String, displayName: String, userTypeId: String): User =
        repository.createMyProfile(email, displayName, userTypeId).toUser()
}