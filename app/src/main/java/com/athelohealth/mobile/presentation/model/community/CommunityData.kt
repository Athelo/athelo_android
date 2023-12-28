package com.athelohealth.mobile.presentation.model.community

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichDocument

data class CommunityData(
    val headline: String? = null,
    val image: String? = null,
    val body: CDARichDocument? = null
) {

    companion object {
        fun getCommunityData(data: CDAEntry): CommunityData {
            val body: CDARichDocument = data.getField("bodyText")
            val image: CDAAsset = data.getField("image")
            return CommunityData(
                headline = "",
                image = "https:" + image.url(),
                body = body
            )
        }
    }
}
