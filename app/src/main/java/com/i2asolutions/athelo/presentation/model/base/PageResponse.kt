package com.i2asolutions.athelo.presentation.model.base

class PageResponse<T>(val result: List<T>, val next: String?) {
}