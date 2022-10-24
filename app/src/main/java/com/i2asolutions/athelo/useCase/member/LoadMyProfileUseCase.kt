package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import com.i2asolutions.athelo.presentation.model.member.User
import javax.inject.Inject

class LoadMyProfileUseCase @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(): User? {
        return repository.runCatching { getMyProfile().results.firstOrNull() }.getOrNull()?.toUser()
    }
}