package com.athelohealth.mobile.useCase.news

import com.athelohealth.mobile.network.repository.news.NewsRepository
import com.athelohealth.mobile.presentation.model.base.PageResponse
import com.athelohealth.mobile.presentation.model.news.Category
import javax.inject.Inject

class LoadNewsCategoriesUseCase @Inject constructor(private val repository: NewsRepository) {
    suspend operator fun invoke(nextUrl: String? = null): PageResponse<Category> {
        return repository.loadNewsCategories(nextUrl).toPageResponse { it.toCategory() }
    }
}