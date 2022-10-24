package com.i2asolutions.athelo.useCase.news

import com.i2asolutions.athelo.network.repository.news.NewsRepository
import com.i2asolutions.athelo.presentation.model.base.PageResponse
import com.i2asolutions.athelo.presentation.model.news.Category
import javax.inject.Inject

class LoadNewsCategoriesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(nextUrl: String? = null): PageResponse<Category> {
        return repository.loadNewsCategories(nextUrl).toPageResponse { it.toCategory() }
    }
}