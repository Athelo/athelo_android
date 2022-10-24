package com.i2asolutions.athelo.network.repository.application

import com.i2asolutions.athelo.extensions.parseResponseWithoutBody
import com.i2asolutions.athelo.network.api.ApplicationApi
import com.i2asolutions.athelo.network.dto.application.ApplicationDto
import com.i2asolutions.athelo.network.dto.application.FAQDto
import com.i2asolutions.athelo.network.dto.application.FeedbackTopicDto
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager

class ApplicationRepositoryImpl(userManager: UserManager) :
    BaseRepository<ApplicationApi>(userManager = userManager, clazz = ApplicationApi::class.java),
    ApplicationRepository {

    override suspend fun loadFAQ(nextUrl: String?): PageResponseDto<FAQDto> {
        return if (nextUrl.isNullOrBlank()) service.getFAQ() else service.getFAQ(nextUrl)
    }

    override suspend fun createFeedback(topicId: String, content: String): Boolean {
        return service.postFeedback(mapOf("topic_id" to topicId, "content" to content)).parseResponseWithoutBody()
    }

    override suspend fun loadFeedbackTopic(): PageResponseDto<FeedbackTopicDto> {
        return service.getFeedbackTopic()
    }

    override suspend fun loadApplications(): PageResponseDto<ApplicationDto> {
        return service.getApplications()
    }

}