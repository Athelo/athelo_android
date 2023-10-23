package com.i2asolutions.athelo.network.repository.news

import com.i2asolutions.athelo.network.api.NewsApi
import com.i2asolutions.athelo.network.dto.base.PageResponseDto
import com.i2asolutions.athelo.network.dto.news.CategoryDto
import com.i2asolutions.athelo.network.dto.news.NewsDto
import com.i2asolutions.athelo.network.repository.BaseRepository
import com.i2asolutions.athelo.utils.UserManager

class NewsRepositoryImpl(userManager: UserManager) :
    BaseRepository<NewsApi>(userManager = userManager, clazz = NewsApi::class.java),
    NewsRepository {
    override suspend fun loadNews(
        nextUrl: String?,
        selectedCategories: List<Int>,
        query: String?
    ): PageResponseDto<NewsDto> {
        return if (!nextUrl.isNullOrBlank()) {
            service.getPosts(nextUrl)
        } else {
            service.getPosts(
                categories = if (selectedCategories.isEmpty()) null else selectedCategories.joinToString(
                    ","
                ),
                searchQuery = query,
            )
        }
    }

    override suspend fun loadNewsDetail(newsId: Int): NewsDto {
        return service.getPostDetail(newsId)
    }

    override suspend fun loadFavouriteNews(
        nextUrl: String?,
        selectedCategories: List<Int>,
        query: String?
    ): PageResponseDto<NewsDto> {
        return if (!nextUrl.isNullOrBlank()) {
            service.getPosts(nextUrl, true)
        } else {
            service.getPosts(
                categories = if (selectedCategories.isEmpty()) null else selectedCategories.joinToString(
                    ","
                ),
                searchQuery = query,
                favourite = true
            )
        }
    }

    override suspend fun loadNewsCategories(nextUrl: String?): PageResponseDto<CategoryDto> {
        return if (nextUrl.isNullOrBlank()) service.getPostCategories() else service.getPostCategories(
            nextUrl
        )
    }

    override suspend fun addToFavourite(newsId: Int): Boolean {
        return service.getAddFavouritePost(newsId).isSuccessful
    }

    override suspend fun removeFromFavourite(newsId: Int): Boolean {
        return service.getRemoveFavouritePost(newsId).isSuccessful
    }
}