package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.utils.AuthorizationException
import javax.inject.Inject

class LoadMyProfileUseCase @Inject constructor(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(): User? {
        val response = repository.runCatching { getMyProfile().results.firstOrNull() }
        return if (response.isSuccess) {
            response.getOrNull()?.toUser()
        } else {
            throw AuthorizationException(throwable = response.exceptionOrNull())
        }
    }

}