package com.i2asolutions.athelo.presentation.ui.share.newsDetail

import com.i2asolutions.athelo.presentation.model.news.News
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class NewsDetailViewState(override val isLoading: Boolean = false, val news: News) :
    BaseViewState

sealed interface NewsDetailEvent : BaseEvent {
    object FavoriteButtonClick : NewsDetailEvent
    object LinkButtonClick : NewsDetailEvent
    object BackButtonClick : NewsDetailEvent
}

sealed interface NewsDetailEffect : BaseEffect {
    object ShowPrevScreen : NewsDetailEffect
    class OpenWebBrowser(val link: String) : NewsDetailEffect
}