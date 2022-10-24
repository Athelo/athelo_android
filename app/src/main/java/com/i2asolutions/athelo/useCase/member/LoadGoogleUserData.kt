package com.i2asolutions.athelo.useCase.member

import com.i2asolutions.athelo.network.repository.member.MemberRepository
import javax.inject.Inject

class LoadGoogleUserData @Inject constructor(private val repository: MemberRepository) {
    suspend operator fun invoke(token: String): String? {
        return repository.loadUserDataFromGoogle(token).email
    }
}