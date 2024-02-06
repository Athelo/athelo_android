package com.athelohealth.mobile.useCase.news

import com.athelohealth.mobile.network.repository.news.NewsRepository
import com.athelohealth.mobile.presentation.model.news.News
import javax.inject.Inject

class RemoveFromFavouriteUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(newsId: Int): News {
        repository.removeFromFavourite(newsId)
        return repository.loadNewsDetail(newsId).toNews()
    }
    suspend operator fun invoke(newsId: String): Boolean {
        return repository.removeFromFavourite(newsId = newsId)
    }
}