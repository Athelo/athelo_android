package com.i2asolutions.athelo.network.repository.member

import com.i2asolutions.athelo.network.api.MemberApi
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.base.RetrofitNullableStringParam
import com.i2asolutions.athelo.network.dto.member.*
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(userManager: UserManager) :
    BaseRepository<MemberApi>(MemberApi::class.java, userManager), MemberRepository {
    override suspend fun register(
        username: String,
        password1: String,
        password2: String
    ): AuthorizeResultDto {
        return service.postSignUp(
            mapOf(
                "email" to username,
                "password1" to password1,
                "password2" to password2
            )
        )
    }

    override suspend fun login(username: String, password: String): AuthorizeResultDto {
        return service.postSignIn(mapOf("email" to username, "password" to password))
    }

    override suspend fun getMyProfile(): PageResponseDto<UserDto> {
        return service.getMyProfile()
    }

    override suspend fun createMyProfile(
        email: String,
        displayName: String,
        atheloUserProfile: String,
    ): UserDto {
        return service.postCreateMyProfile(
            mapOf(
                "email" to email,
                "athelo_user_type" to atheloUserProfile,
                "display_name" to displayName,
            )
        )
    }

    override suspend fun resetPasswordRequest(email: String): ResetPasswordResultDto {
        return service.postPasswordResetRequest(mapOf("email" to email))
    }

    override suspend fun getPersonProfile(userId: Int): UserDto {
        return service.getUserProfile(userId)
    }

    override suspend fun updateUserProfile(
        userId: Int,
        phoneNumber: String?,
        birthDate: String?,
        email: String,
        displayName: String,
        userType: String?
    ): UserDto {
        return service.updateUserProfile(
            userId,
            UpdateUserProfileBodyDto(
                email = email,
                displayName = displayName,
                phoneNumber = RetrofitNullableStringParam(phoneNumber),
                birthdate = birthDate,
                userType = userType
            )
        )
    }

    override suspend fun updateUserPhoto(userId: Int, photoId: Int): UserDto {
        return service.updateUserPhoto(userId, UpdateUserPhotoBodyDto(photoId))
    }

    override suspend fun loadMoreUsers(url: String): PageResponseDto<UserDto> {
        return service.getUserProfiles(url)
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        repeatPassword: String
    ): String {
        val response = service.postChangePassword(
            mapOf(
                "old_password" to oldPassword,
                "new_password1" to newPassword,
                "new_password2" to repeatPassword
            )
        )
        return response
    }

    override suspend fun loginSocial(token: String, source: String): AuthorizeResultDto {
        return service.postLoginSocial(
            mapOf(
                "third_party_access_token_source" to source,
                "third_party_access_token" to token
            )
        )
    }

    override suspend fun loadUserDataFromGoogle(token: String): GoogleUserDto {
        return service.getUserData(token)
    }

    override suspend fun deleteAccount(): Boolean {
        return service.deleteUser().isSuccessful
    }

    override suspend fun checkAuthorizationIdentity(): List<AuthorizationIdentityDto> {
        return service.getAuthorizationIdentity()
    }
}