package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import dagger.Lazy
import javax.inject.Inject

class UserAuthorizationTestUseCase @Inject constructor(private val repository: Lazy<MemberRepository>) {

    suspend operator fun invoke(): Boolean =
        repository.runCatching { get().getMyProfile().results.first().toUser() }
            .mapCatching { true }
            .getOrDefault(false)
}