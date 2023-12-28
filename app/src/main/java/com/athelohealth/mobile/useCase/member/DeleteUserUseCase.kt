package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: MemberRepository
) {

    suspend operator fun invoke() =
        repository.deleteAccount()
}