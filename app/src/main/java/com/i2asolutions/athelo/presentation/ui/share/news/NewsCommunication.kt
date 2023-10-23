package com.i2asolutions.athelo.presentation.ui.share.news

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.model.news.Category
import com.i2asolutions.athelo.presentation.model.news.News
import com.i2asolutions.athelo.presentation.model.news.NewsListType
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

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
    class InputValueChanged(val inputValue: InputType) : NewsEvent
    class SearchCategoriesUpdate(val selectedCategory: List<Category>) : NewsEvent
    class NewsItemClick(val news: News) : NewsEvent
}

sealed interface NewsEffect : BaseEffect {
    object ShowMenuScreen : NewsEffect
    object ShowMyProfileScreen : NewsEffect
    class OpenNewsDetailScreen(val news: News) : NewsEffect
    class ShowCategoryFilter(val initialSelection: List<Category>) : NewsEffect
}