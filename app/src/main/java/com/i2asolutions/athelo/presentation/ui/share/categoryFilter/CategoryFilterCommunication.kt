package com.i2asolutions.athelo.presentation.ui.share.categoryFilter

import com.i2asolutions.athelo.presentation.model.news.Category
import com.i2asolutions.athelo.presentation.ui.base.BaseEffect
import com.i2asolutions.athelo.presentation.ui.base.BaseEvent
import com.i2asolutions.athelo.presentation.ui.base.BaseViewState

data class CategoryFilterViewState(
    override val isLoading: Boolean = true,
    val selectedFilters: List<Category> = emptyList(),
    val allFilters: List<Category> = emptyList(),
    val canLoadMore: Boolean = false,
) : BaseViewState

sealed interface CategoryFilterEvent : BaseEvent {
    object SearchClick : CategoryFilterEvent
    object CancelClick : CategoryFilterEvent
    class CategoryClick(val category: Category) : CategoryFilterEvent
}

sealed interface CategoryFilterEffect : BaseEffect {
    object ShowPrevScreen : CategoryFilterEffect
    class SendNewSelectedScreen(val selectedCategories: List<Category>) : CategoryFilterEffect
}