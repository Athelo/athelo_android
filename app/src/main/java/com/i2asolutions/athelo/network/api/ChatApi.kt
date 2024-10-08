package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.chat.ChatTokenDto
import com.i2asolutions.athelo.network.dto.chat.ConversationDto
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {
    @POST("/api/v1/chats/sessions/open-session/")
    suspend fun postOpenSession(@Body body: Map<String, String>): Response<ChatTokenDto>

    @POST("/api/v1/chats/sessions/close-session/")
    suspend fun postCloseSession(@Body body: Map<String, String>): Response<Unit>

    @GET
    suspend fun getGroupConversations(@Url url: String = "api/v1/chats/group-conversations/"): PageResponseDto<ConversationDto>

    @GET("api/v1/chats/group-conversations/{id}/")
    suspend fun getGroupConversation(@Path("id") conversationId: Int): ConversationDto

    @GET("api/v1/chats/group-conversations/{id}/join/")
    suspend fun getJoinConversation(@Path("id") conversationId: Int): Response<Unit>

    @GET("api/v1/chats/group-conversations/{id}/leave/")
    suspend fun getLeaveConversation(@Path("id") conversationId: Int): Response<Unit>
}