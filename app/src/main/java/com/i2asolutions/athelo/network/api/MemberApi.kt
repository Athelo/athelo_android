package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.member.*
import retrofit2.Response
import retrofit2.http.*

interface MemberApi {
    @POST("api/v1/common/i2a-oauth2/register/")
    suspend fun postSignUp(@Body body: Map<String, String>): AuthorizeResultDto

    @POST("/api/v1/common/i2a-oauth2/login-social/")
    suspend fun postLoginSocial(@Body body: Map<String, String>): AuthorizeResultDto

    @POST("api/v1/common/i2a-oauth2/login/")
    suspend fun postSignIn(@Body body: Map<String, String>): AuthorizeResultDto

    @GET("api/v1/users/me/")
    suspend fun getMyProfile(): PageResponseDto<UserDto>

    @POST("api/v1/users/me/")
    suspend fun postCreateMyProfile(@Body body: Map<String, String>): UserDto

    @POST("api/v1/common/i2a-oauth2/password-reset-request/generate-link/")
    suspend fun postPasswordResetRequest(@Body body: Map<String, String>): ResetPasswordResultDto

    @GET("api/v1/users/user-profiles/{id}")
    suspend fun getUserProfile(@Path("id") id: Int): UserDto

    @PATCH("api/v1/users/me/{id}/")
    suspend fun updateUserProfile(
        @Path("id") id: Int,
        @Body body: UpdateUserProfileBodyDto
    ): UserDto

    @PATCH("api/v1/users/me/{id}/")
    suspend fun updateUserPhoto(
        @Path("id") id: Int,
        @Body body: UpdateUserPhotoBodyDto
    ): UserDto

    @GET
    suspend fun getUserProfiles(@Url url: String = "api/v1/users/user-profiles/"): PageResponseDto<UserDto>

    @POST("api/v1/common/i2a-oauth2/password-change/")
    suspend fun postChangePassword(@Body body: Map<String, String>): String

    @GET("https://www.googleapis.com/oauth2/v1/userinfo")
    suspend fun getUserData(@Query("access_token") token: String): GoogleUserDto

    @DELETE("/api/v1/users/me/delete/")
    suspend fun deleteUser(): Response<Unit>

    @GET("api/v1/users/me/authorization-identities/")
    suspend fun getAuthorizationIdentity(): List<AuthorizationIdentityDto>
}