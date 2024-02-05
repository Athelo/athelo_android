package com.athelohealth.mobile.network.repository.member

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.member.*
import retrofit2.Response

interface MemberRepository {
    suspend fun register(
        username: String,
        password1: String,
        password2: String
    ): AuthorizeResultDto

    suspend fun login(username: String, password: String): AuthorizeResultDto

    suspend fun getMyProfile(): PageResponseDto<UserDto>

    suspend fun createMyProfile(
        email: String,
        displayName: String,
        atheloUserProfile: String,
    ): UserDto

    suspend fun resetPasswordRequest(email: String): ResetPasswordResultDto

    suspend fun getPersonProfile(userId: Int): UserDto

    suspend fun updateUserProfile(
        userId: Int,
        phoneNumber: String?,
        birthDate: String?,
        email: String,
        displayName: String,
        userType: String?,
    ): UserDto

    suspend fun getPatientStatus(): PatientStatus

    suspend fun updateCancerStatus(cancerStatus: CancerStatus): PatientStatus

    suspend fun updateUserPhoto(userId: Int, photoId: Int): UserDto

    suspend fun loadMoreUsers(url: String): PageResponseDto<UserDto>

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        repeatPassword: String
    ): String

    suspend fun loginSocial(token: String, source: String): AuthorizeResultDto

    suspend fun loadUserDataFromGoogle(token: String): GoogleUserDto
    suspend fun deleteAccount(): Boolean

    suspend fun postUserProfile(displayName: String): Response<Unit>
}