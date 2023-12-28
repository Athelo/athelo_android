package com.athelohealth.mobile.presentation.model.news

import com.athelohealth.mobile.presentation.model.base.Image

data class News(
    val id: Int = -1,
    var isFavourite: Boolean = false,
    val name: String = "",
    val description: String = "",
    val categories: List<String> = emptyList(),
    val image: Image? = null,
    val articleLink: String? = null,
    val createDate: String = ""
)