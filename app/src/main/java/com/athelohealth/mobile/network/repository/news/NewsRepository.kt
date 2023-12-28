package com.athelohealth.mobile.network.repository.news

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.news.CategoryDto
import com.athelohealth.mobile.network.dto.news.NewsDto

interface NewsRepository {
    suspend fun loadNews(
        nextUrl: String?,
        selectedCategories: List<Int>,
        query: String?
    ): PageResponseDto<NewsDto>

    suspend fun loadNewsDetail(
        newsId: Int
    ): NewsDto

    suspend fun loadFavouriteNews(
        nextUrl: String?,
        selectedCategories: List<Int>,
        query: String?
    ): PageResponseDto<NewsDto>

    suspend fun loadNewsCategories(nextUrl: String?): PageResponseDto<CategoryDto>

    suspend fun addToFavourite(newsId: Int): Boolean
    suspend fun removeFromFavourite(newsId: Int): Boolean
}