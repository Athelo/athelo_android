package com.athelohealth.mobile.presentation.ui.share.news

import com.athelohealth.mobile.presentation.model.member.User
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.model.news.News
import com.athelohealth.mobile.presentation.model.news.NewsData
import com.athelohealth.mobile.presentation.model.news.NewsListType
import com.athelohealth.mobile.presentation.ui.base.BaseEffect
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BaseViewState

data class NewsViewState(
    val initialized: Boolean = false,
    override val isLoading: Boolean = true,
    val news: List<News> = emptyList(),
    val canLoadMore: Boolean = false,
    val screenType: NewsListType = NewsListType.List,
    val searchQuery: String = "",
    val currentUser: User = User()
) : BaseViewState

sealed interface NewsEvent : BaseEvent {
    object MenuClick : NewsEvent
    object MyProfileClick : NewsEvent
    object FilterOptionClick : NewsEvent
    object FavoriteButtonClick : NewsEvent
    object ListButtonClick : NewsEvent
    object LoadFirstPage : NewsEvent
    object LoadNextPage : NewsEvent
    object RefreshList : NewsEvent
    object SearchClick : NewsEvent
    class SearchCategoriesUpdate(val selectedCategory: List<Category>) : NewsEvent
    class NewsItemClick(val news: News) : NewsEvent
    class NewsDetails(val news: NewsData) : NewsEvent
}

sealed interface NewsEffect : BaseEffect {
    object ShowMenuScreen : NewsEffect
    object ShowMyProfileScreen : NewsEffect
    class OpenNewsDetailScreen(val news: News) : NewsEffect
    class ShowCategoryFilter(val initialSelection: List<Category>) : NewsEffect
    class OpenContentfulNewsDetailScreen(val news: NewsData) : NewsEffect
}