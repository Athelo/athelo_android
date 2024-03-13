package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import retrofit2.Response
import javax.inject.Inject

class PostUserProfile @Inject constructor(private val memberRepository: MemberRepository) {

    suspend operator fun invoke(userName: String, code: String): Response<Unit> {
        return memberRepository.postUserProfile(displayName = userName, code = code)
    }
}