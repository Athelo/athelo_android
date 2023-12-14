package com.i2asolutions.athelo.presentation.model.news

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichDocument
import com.i2asolutions.athelo.extensions.toDate
import java.util.Date

data class NewsData(
    val key: String = "",
    val title: String = "",
    val image: String = "",
    val body: CDARichDocument = CDARichDocument(),
    val date: Date = Date(),
    val shouldOpenInBrowser: Boolean = false,
    val browserUrl: String? = null,
    val bottomLogo: String = "",

) {

    companion object {
        fun getNewsData(key: String, data: CDAEntry): NewsData {
            val title: String = data.getField("title")
            val body: CDARichDocument = data.getField("body")
            val image: CDAAsset? = data.getField("articleImage")
            val shouldOpenInBrowser: Boolean = data.getField("shouldOpenInBrowser") ?: false
            val browserUrl: String? = data.getField("browserUrl")
            val bottomLogo: CDAAsset? = data.getField("bottomLogo")
            val date = data.getAttribute<String>("updatedAt").toDate() ?: Date()
            return NewsData(
                key = key,
                title = title,
                image = image?.let { "https:" + it.url() } ?: "",
                body = body,
                date = date,
                shouldOpenInBrowser = shouldOpenInBrowser,
                browserUrl = browserUrl,
                bottomLogo = bottomLogo?.let { "https:" + it.url() } ?: ""
            )
        }
    }
}
