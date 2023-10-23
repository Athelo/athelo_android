package com.i2asolutions.athelo.presentation.model.news

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichDocument

data class NewsData(
    val key: String = "",
    val title: String = "",
    val image: String = "",
    val body: CDARichDocument = CDARichDocument()
) {

    companion object {
        fun getNewsData(key: String, data: CDAEntry): NewsData {
            val title: String = data.getField("title")
            val body: CDARichDocument = data.getField("body")
            val image: CDAAsset = data.getField("articleImage")
            return NewsData(
                key = key,
                title = title,
                image = "https:" + image.url(),
                body = body
            )
        }
    }
}
