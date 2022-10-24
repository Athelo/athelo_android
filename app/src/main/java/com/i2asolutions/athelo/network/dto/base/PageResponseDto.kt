package com.i2asolutions.athelo.network.dto.base

import com.i2asolutions.athelo.presentation.model.base.PageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PageResponseDto<T>(
    @SerialName("results") val results: List<T>,
    @SerialName("next") val nextUrl: String?
) {
    inline fun <R> toPageResponse(map: (T) -> R?): PageResponse<R> {
        return PageResponse(results.mapNotNull(map), nextUrl)
    }
}