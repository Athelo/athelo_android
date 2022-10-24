package com.i2asolutions.athelo.network.repository.application

import com.i2asolutions.athelo.network.dto.application.ApplicationDto
import com.i2asolutions.athelo.network.dto.application.FAQDto
import com.i2asolutions.athelo.network.dto.application.FeedbackTopicDto
import com.i2asolutions.athelo.network.dto.base.PageResponseDto

interface ApplicationRepository {

    suspend fun loadFAQ(nextUrl: String?): PageResponseDto<FAQDto>

    suspend fun createFeedback(topicId: String, content: String): Boolean

    suspend fun loadFeedbackTopic(): PageResponseDto<FeedbackTopicDto>

    suspend fun loadApplications(): PageResponseDto<ApplicationDto>
}