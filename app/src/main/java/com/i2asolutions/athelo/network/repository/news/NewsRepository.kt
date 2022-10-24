package com.i2asolutions.athelo.network.repository.news

import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.news.CategoryDto
import com.i2asolutions.athelo.network.dto.news.NewsDto

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