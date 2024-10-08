package com.i2asolutions.athelo.network.api

import com.i2asolutions.athelo.network.dto.application.ApplicationDto
import com.i2asolutions.athelo.network.dto.application.FAQDto
import com.i2asolutions.athelo.network.dto.application.FeedbackTopicDto
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApplicationApi {

    @GET
    suspend fun getFAQ(@Url url: String = "api/v1/applications/frequently_asked_question/"): PageResponseDto<FAQDto>

    @GET("api/v1/applications/feedback-topic/")
    suspend fun getFeedbackTopic(): PageResponseDto<FeedbackTopicDto>

    @POST("api/v1/applications/feedback/")
    suspend fun postFeedback(@Body body: Map<String,String>): Response<Unit>

    @GET("api/v1/applications/applications/")
    suspend fun getApplications(): PageResponseDto<ApplicationDto>
}