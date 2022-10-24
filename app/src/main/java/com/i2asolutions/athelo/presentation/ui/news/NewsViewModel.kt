package com.i2asolutions.athelo.presentation.ui.news

import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.news.Category
import com.i2asolutions.athelo.presentation.model.news.NewsListType
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.member.LoadCachedUserUseCase
import com.i2asolutions.athelo.useCase.member.LoadMyProfileUseCase
import com.i2asolutions.athelo.useCase.member.StoreUserUseCase
import com.i2asolutions.athelo.useCase.news.LoadFavouriteNewsUseCase
import com.i2asolutions.athelo.useCase.news.LoadNewsUseCase
import com.i2asolutions.athelo.utils.AuthorizationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val loadNews: LoadNewsUseCase,
    private val loadMyProfileUseCase: LoadMyProfileUseCase,
    private val storeProfile: StoreUserUseCase,
    private val loadFavouriteNews: LoadFavouriteNewsUseCase,
    private val loadCachedUserUseCase: LoadCachedUserUseCase,
) :
    BaseViewModel<NewsEvent, NewsEffect>() {

    private var currentState = NewsViewState()

    private val _state = MutableStateFlow(currentState)
    val viewState = _state.asStateFlow()

    private var newsNextUrl: String? = null
    private var selectedCategories = listOf<Category>()
    private var query: String? = null
    private var currentScreenType: NewsListType = NewsListType.List

    override fun loadData() {
        notifyStateChanged(currentState.copy(isLoading = true))
        launchRequest {
            val user =
                loadCachedUserUseCase() ?: loadMyProfileUseCase().also { storeProfile(it) } ?: throw AuthorizationException()
            val news = if (currentScreenType == NewsListType.List) loadNews(
                newsNextUrl,
                selectedCategories.map { it.id },
                query
            ) else loadFavouriteNews(
                newsNextUrl,
                selectedCategories.map { it.id },
                query
            )
            notifyStateChanged(
                currentState.copy(
                    initialized = true,
                    isLoading = false,
                    news = news.result,
                    currentUser = user,
                    screenType = currentScreenType,
                    canLoadMore = !news.next.isNullOrBlank(),
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
            is NewsEvent.InputValueChanged -> handleInputChanged(event.inputValue)
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
        }
    }

    private fun updateCategoriesAndReloadList(newCategories: List<Category>) {
        selectedCategories = newCategories
        resetAndLoad()
    }

    private fun handleInputChanged(inputType: InputType) {
        when (inputType) {
            is InputType.Text -> {
                query = inputType.value
            }
            else -> { /*Ignore other types*/
            }
        }
    }

    private fun displayFavouriteMode() {
        currentScreenType = NewsListType.Favourites
        resetAndLoad()
    }

    private fun displayListMode() {
        currentScreenType = NewsListType.List
        resetAndLoad()
    }

    private fun notifyStateChanged(newState: NewsViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
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
}