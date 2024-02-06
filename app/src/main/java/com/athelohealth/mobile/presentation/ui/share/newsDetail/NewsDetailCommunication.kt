package com.athelohealth.mobile.presentation.ui.share.newsDetail

import com.athelohealth.mobile.presentation.model.news.News
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class NewsDetailViewState(
    override val isLoading: Boolean = false,
    val isFavourite: Boolean = false,
    val news: News
) : BaseViewState

sealed interface NewsDetailEvent : BaseEvent {
    object FavoriteButtonClick : NewsDetailEvent
    object LinkButtonClick : NewsDetailEvent
    object BackButtonClick : NewsDetailEvent
}

sealed interface NewsDetailEffect : BaseEffect {
    object ShowPrevScreen : NewsDetailEffect
    class OpenWebBrowser(val link: String) : NewsDetailEffect
}