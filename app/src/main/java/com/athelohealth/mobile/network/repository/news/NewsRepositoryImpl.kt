package com.athelohealth.mobile.network.repository.news

import com.athelohealth.mobile.network.api.NewsApi
import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.news.CategoryDto
import com.athelohealth.mobile.network.dto.news.NewsDto
import com.athelohealth.mobile.network.repository.BaseRepository
import com.athelohealth.mobile.utils.UserManager
import com.athelohealth.mobile.network.dto.news.FavouriteDto


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
    override suspend fun getFavouriteNews(): PageResponseDto<FavouriteDto> {

        return service.getFavouritePost()
    }

    override suspend fun addToFavourite(newsId: String): Boolean {
        return service.getAddFavouritePost(FavouriteDto(externalContentId = newsId)).isSuccessful
    }

    override suspend fun removeFromFavourite(newsId: String): Boolean {
        return service.getRemoveFavouritePost(FavouriteDto(externalContentId = newsId)).isSuccessful
    }
}