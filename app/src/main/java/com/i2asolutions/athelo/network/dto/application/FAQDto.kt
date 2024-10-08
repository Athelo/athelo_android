package com.i2asolutions.athelo.network.dto.application


import com.i2asolutions.athelo.presentation.model.application.FAQ
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FAQDto(
    @SerialName("content")
    val content: String? = null,
    @SerialName("question")
    val question: String? = null
) {
    fun toFAQ(): FAQ {
        return FAQ(header = question?.trim() ?: "", content = content?.trim() ?: "")
    }
}