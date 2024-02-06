package com.athelohealth.mobile.useCase.news

import com.athelohealth.mobile.network.repository.news.NewsRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.news.FavouriteContent
import com.athelohealth.mobile.presentation.model.news.News
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

    suspend operator fun invoke() : PageResponse<FavouriteContent> {
        return repository.getFavouriteNews().toPageResponse { it.toFavouriteContent() }
    }
}