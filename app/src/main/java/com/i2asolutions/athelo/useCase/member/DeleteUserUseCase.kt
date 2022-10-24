package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: MemberRepository
) {

    suspend operator fun invoke() =
        repository.deleteAccount()
}