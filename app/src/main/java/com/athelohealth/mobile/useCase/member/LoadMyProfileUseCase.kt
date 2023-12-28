package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.User
import javax.inject.Inject

class LoadMyProfileUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(): User? {
        return repository.runCatching { getMyProfile().results.firstOrNull() }.getOrNull()?.toUser()
    }
}