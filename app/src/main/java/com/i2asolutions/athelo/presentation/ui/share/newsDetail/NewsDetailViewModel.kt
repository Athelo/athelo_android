package com.i2asolutions.athelo.presentation.ui.share.newsDetail

import androidx.lifecycle.SavedStateHandle
import com.i2asolutions.athelo.presentation.model.news.News
import com.i2asolutions.athelo.presentation.model.news.NewsData
import com.i2asolutions.athelo.presentation.ui.base.BaseViewModel
import com.i2asolutions.athelo.useCase.news.AddToFavouriteUseCase
import com.i2asolutions.athelo.useCase.news.LoadNewsDetailUseCase
import com.i2asolutions.athelo.useCase.news.RemoveFromFavouriteUseCase
import com.i2asolutions.athelo.utils.contentful.ContentfulClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val loadNewsDetail: LoadNewsDetailUseCase,
    private val addToFavourite: AddToFavouriteUseCase,
    private val removeFromFavourite: RemoveFromFavouriteUseCase,
    private val contentfulClient: ContentfulClient,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<NewsDetailEvent, NewsDetailEffect>() {
    private val newsId: String = NewsDetailFragmentArgs.fromSavedStateHandle(savedStateHandle).newsId
    private lateinit var news: News
    private var currentState = NewsDetailViewState(true, News())

    private val _state = MutableStateFlow(currentState)
    val state = _state.asStateFlow()
    private val _contentfulViewState = MutableStateFlow(NewsData())
    val contentfulViewState = _contentfulViewState.asStateFlow()

    override fun handleError(throwable: Throwable) {
        notifyStateChanged(currentState.copy(isLoading = false))
        super.handleError(throwable)
    }

    override fun loadData() {
        launchRequest {
//            news = loadNewsDetail(newsId)
            _contentfulViewState.emit(contentfulClient.getNewsById(newsId))
            notifyStateChanged(currentState.copy(isLoading = false))
        }
    }

    override fun handleEvent(event: NewsDetailEvent) {
        when (event) {
            NewsDetailEvent.BackButtonClick -> notifyEffectChanged(NewsDetailEffect.ShowPrevScreen)
            NewsDetailEvent.FavoriteButtonClick -> selfBlockRun { toggleFavouriteState() }
            NewsDetailEvent.LinkButtonClick -> selfBlockRun {
                news.articleLink?.let { url ->
                    notifyEffectChanged(NewsDetailEffect.OpenWebBrowser(url))
                }
            }
        }
    }

    private fun toggleFavouriteState() = launchRequest {
        notifyStateChanged(currentState.copy(isLoading = true))
        news = if (news.isFavourite) {
            removeFromFavourite(0)
        } else {
            addToFavourite(0)
        }
        notifyStateChanged(currentState.copy(isLoading = false, news = news))
    }

    private fun notifyStateChanged(newState: NewsDetailViewState) {
        currentState = newState
        launchOnUI { _state.emit(currentState) }
    }
}