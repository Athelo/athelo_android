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
    val date: Date = Date()
) {

    companion object {
        fun getNewsData(key: String, data: CDAEntry): NewsData {
            val title: String = data.getField("title")
            val body: CDARichDocument = data.getField("body")
            val image: CDAAsset? = data.getField("articleImage")
            val date = data.getAttribute<String>("updatedAt").toDate() ?: Date()
            return NewsData(
                key = key,
                title = title,
                image = image?.let { "https:" + it.url() } ?: "",
                body = body,
                date = date
            )
        }
    }
}
