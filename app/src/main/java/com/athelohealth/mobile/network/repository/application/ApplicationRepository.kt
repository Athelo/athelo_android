package com.athelohealth.mobile.network.repository.application

import com.athelohealth.mobile.network.dto.application.ApplicationDto
import com.athelohealth.mobile.network.dto.application.FAQDto
import com.athelohealth.mobile.network.dto.application.FeedbackTopicDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto

interface ApplicationRepository {

    suspend fun loadFAQ(nextUrl: String?): PageResponseDto<FAQDto>

    suspend fun createFeedback(topicId: String, content: String): Boolean

    suspend fun loadFeedbackTopic(): PageResponseDto<FeedbackTopicDto>

    suspend fun loadApplications(): PageResponseDto<ApplicationDto>
}