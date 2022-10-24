package com.i2asolutions.athelo.useCase.news

import com.i2asolutions.athelo.network.repository.news.NewsRepository
import com.i2asolutions.athelo.presentation.model.news.News
import javax.inject.Inject

class AddToFavouriteUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(newsId: Int): News {
        repository.addToFavourite(newsId)
        return repository.loadNewsDetail(newsId).toNews()
    }
}