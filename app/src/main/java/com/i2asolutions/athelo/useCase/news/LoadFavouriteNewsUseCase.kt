package com.i2asolutions.athelo.useCase.news

import com.i2asolutions.athelo.network.repository.news.NewsRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.news.News
import javax.inject.Inject

class LoadFavouriteNewsUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(
        nextUrl: String?,
        selectedCategories: List<Int>,
        query: String?
    ): PageResponse<News> {
        return repository.loadFavouriteNews(
            nextUrl = nextUrl,
            selectedCategories = selectedCategories,
            query = query
        ).toPageResponse { it.toNews() }
    }
}