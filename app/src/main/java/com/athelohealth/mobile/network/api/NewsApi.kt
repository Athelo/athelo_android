package com.athelohealth.mobile.network.api

import com.athelohealth.mobile.network.dto.base.PageResponseDto
import com.athelohealth.mobile.network.dto.news.CategoryDto
import com.athelohealth.mobile.network.dto.news.FavouriteDto
import com.athelohealth.mobile.network.dto.news.NewsDto
import retrofit2.Response
import retrofit2.http.*

interface NewsApi {

    @GET
    suspend fun getPosts(
        @Url url: String = "api/v1/posts/posts/",
        @Query("is_favourite") favourite: Boolean
    ): PageResponseDto<NewsDto>

    @GET
    suspend fun getPosts(
        @Url url: String = "api/v1/posts/posts/",
    ): PageResponseDto<NewsDto>

    @GET
    suspend fun getPosts(
        @Url url: String = "api/v1/posts/posts/",
        @Query("categories__id__in") categories: String?,
        @Query("title__icontains") searchQuery: String?,
        @Query("is_favourite") favourite: Boolean
    ): PageResponseDto<NewsDto>

    @GET
    suspend fun getPosts(
        @Url url: String = "api/v1/posts/posts/",
        @Query("categories__id__in") categories: String?,
        @Query("title__icontains") searchQuery: String?,
    ): PageResponseDto<NewsDto>

    @GET
    suspend fun getPostCategories(@Url url: String = "api/v1/posts/post-categories/"): PageResponseDto<CategoryDto>

    @POST("/api/v1/posts/posts/{id}/add_to_favourites/")
    suspend fun getAddFavouritePost(@Path("id") id: Int): Response<Unit>

    @POST("/api/v1/posts/posts/{id}/remove_from_favourites/")
    suspend fun getRemoveFavouritePost(@Path("id") id: Int): Response<Unit>

    @GET("api/v1/posts/posts/{id}/")
    suspend fun getPostDetail(@Path("id") id: Int): NewsDto

    @GET("api/v1/saved-content/")
    suspend fun getFavouritePost(): PageResponseDto<FavouriteDto>

    @POST("/api/v1/saved-content/")
    suspend fun getAddFavouritePost(@Body body: FavouriteDto): Response<Unit>

    @HTTP(method = "DELETE", path = "/api/v1/saved-content/", hasBody = true)
    suspend fun getRemoveFavouritePost(@Body body: FavouriteDto): Response<Unit>

}