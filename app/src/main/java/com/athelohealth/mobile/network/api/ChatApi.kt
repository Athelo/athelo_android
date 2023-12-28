package com.athelohealth.mobile.network.api

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.chat.ChatTokenDto
import com.athelohealth.mobile.network.dto.chat.ConversationDto
import com.athelohealth.mobile.network.dto.chat.PrivateConversationDto
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {
    @POST("/api/v1/chats/sessions/open-session/")
    suspend fun postOpenSession(@Body body: Map<String, String>): Response<ChatTokenDto>

    @POST("/api/v1/chats/sessions/close-session/")
    suspend fun postCloseSession(@Body body: Map<String, String>): Response<Unit>

    @GET
    suspend fun getGroupConversations(@Url url: String = "api/v1/chats/group-conversations/"): PageResponseDto<ConversationDto>

    @GET
    suspend fun getPrivateConversations(
        @Url url: String = "api/v1/chats/conversations",
        @Query(
            "contains__user_ids",
            encoded = true
        ) forUsersIds: String? = null
    ): PageResponseDto<PrivateConversationDto>

    @GET("api/v1/chats/group-conversations/{id}/")
    suspend fun getGroupConversation(@Path("id") conversationId: Int): ConversationDto

    @GET("api/v1/chats/conversations/{id}/")
    suspend fun getConversation(@Path("id") conversationId: Int): PrivateConversationDto

    @GET("api/v1/chats/group-conversations/{id}/join/")
    suspend fun getJoinConversation(@Path("id") conversationId: Int): Response<Unit>

    @GET("api/v1/chats/group-conversations/{id}/leave/")
    suspend fun getLeaveConversation(@Path("id") conversationId: Int): Response<Unit>

    @POST("api/v1/chats/conversations/")
    suspend fun postCreateConversation(@Body body: Map<String, String>): Response<PrivateConversationDto>
}