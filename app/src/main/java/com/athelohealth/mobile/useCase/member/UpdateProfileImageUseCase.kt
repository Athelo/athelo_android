package com.athelohealth.mobile.useCase.member

import com.athelohealth.mobile.network.repository.member.MemberRepository
import com.athelohealth.mobile.presentation.model.member.User
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(private val repository: MemberRepository) {

    suspend operator fun invoke(userId: Int, photoId: Int): User =
        repository.updateUserPhoto(userId, photoId).toUser()
}