package com.athelohealth.mobile.presentation.ui.share.news

import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.model.news.NewsData
import com.athelohealth.mobile.presentation.model.news.NewsListType
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.useCase.member.LoadCachedUserUseCase
import com.athelohealth.mobile.useCase.member.LoadMyProfileUseCase
import com.athelohealth.mobile.useCase.member.StoreUserUseCase
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.contentful.ContentfulClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeProfile: StoreUserUseCase,
    private val loadCachedUserUseCase: LoadCachedUserUseCase,
    private val contentfulClient: ContentfulClient,
) :
    BaseViewModel<NewsEvent, NewsEffect, NewsViewState>(NewsViewState()) {

    private var newsList: List<NewsData> = listOf()

    private val _contentfulViewState = MutableStateFlow(listOf<NewsData>())
    val contentfulViewState = _contentfulViewState.asStateFlow()

    private var newsNextUrl: String? = null
    private var selectedCategories = listOf<Category>()
    private var query: String = ""
    private var currentScreenType: NewsListType = NewsListType.List

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = true))
        launchRequest {
            val user = loadCachedUserUseCase() ?: loadMyProfileUseCase().also { storeProfile(it) } ?: throw AuthorizationException()
            newsList = contentfulClient.getAllNews()
            _contentfulViewState.emit(currentNewsList())
            notifyStateChange(
                currentState.copy(
                    initialized = true,
                    isLoading = false,
                    currentUser = user,
                    screenType = currentScreenType
                )
            )
        }
    }

    override fun handleEvent(event: NewsEvent) {
        when (event) {
            NewsEvent.MyProfileClick -> notifyEffectChanged(NewsEffect.ShowMyProfileScreen)
            NewsEvent.MenuClick -> notifyEffectChanged(NewsEffect.ShowMenuScreen)
            NewsEvent.FavoriteButtonClick -> displayFavouriteMode()
            NewsEvent.ListButtonClick -> displayListMode()
            NewsEvent.FilterOptionClick -> {
                notifyEffectChanged(NewsEffect.ShowCategoryFilter(initialSelection = selectedCategories.toList()))
            }
            NewsEvent.LoadNextPage -> loadNextPageIfExist()
            is NewsEvent.NewsItemClick -> notifyEffectChanged(NewsEffect.OpenNewsDetailScreen(event.news))
            NewsEvent.RefreshList -> resetAndLoad()
            is NewsEvent.SearchCategoriesUpdate -> {
                updateCategoriesAndReloadList(event.selectedCategory)
            }
            NewsEvent.SearchClick -> {
                resetAndLoad()
            }
            NewsEvent.LoadFirstPage -> {
                resetAndLoad()
            }
            is NewsEvent.NewsDetails -> {
                notifyEffectChanged(NewsEffect.OpenContentfulNewsDetailScreen(news = event.news))
            }
        }
    }

    private fun updateCategoriesAndReloadList(newCategories: List<Category>) {
        selectedCategories = newCategories
        resetAndLoad()
    }

    private fun displayFavouriteMode() {
        currentScreenType = NewsListType.Favourites
        resetAndLoad()
    }

    private fun displayListMode() {
        currentScreenType = NewsListType.List
        resetAndLoad()
    }


    private fun resetAndLoad() {
        newsNextUrl = null
        loadData()
    }

    private fun loadNextPageIfExist() {
        if (!newsNextUrl.isNullOrBlank()) {
            loadData()
        }
    }

    fun updateNewsList(title: String) {
        launchRequest {
            query = title
            _contentfulViewState.emit(currentNewsList())
        }
    }

    /**
     * This will check and return if the current selected option is `All News` or `Favourites`
     */
    private fun currentNewsList(): List<NewsData> {
        return if (currentScreenType == NewsListType.List) newsList else {
            if (newsList.isNotEmpty()) listOf(newsList.last()) else listOf()
        }.filter {
            if (query.trim().isNotEmpty()) it.title.contains(query.trim(), ignoreCase = true) else true
        }
    }

    fun currentQuery() = query

}