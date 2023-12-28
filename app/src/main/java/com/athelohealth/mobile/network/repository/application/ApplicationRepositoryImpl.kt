package com.athelohealth.mobile.network.repository.application

import com.athelohealth.mobile.extensions.parseResponseWithoutBody
import com.athelohealth.mobile.network.api.ApplicationApi
import com.athelohealth.mobile.network.dto.application.ApplicationDto
import com.athelohealth.mobile.network.dto.application.FAQDto
import com.athelohealth.mobile.network.dto.application.FeedbackTopicDto
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager

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