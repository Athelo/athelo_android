package com.athelohealth.mobile.useCase.news

import com.athelohealth.mobile.network.repository.news.NewsRepository
import com.athelohealth.mobile.presentation.model.news.News
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(newsId: Int): News {
        repository.addToFavourite(newsId)
        return repository.loadNewsDetail(newsId).toNews()
    }
}