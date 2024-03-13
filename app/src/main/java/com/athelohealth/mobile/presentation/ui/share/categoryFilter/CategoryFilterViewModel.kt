package com.athelohealth.mobile.presentation.ui.share.categoryFilter

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.utils.contentful.ContentfulClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryFilterViewModel @Inject constructor(
    private val contentfulClient: ContentfulClient,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CategoryFilterEvent, CategoryFilterEffect, CategoryFilterViewState>(CategoryFilterViewState()) {
    private val initialCategories =
        CategoryFilterDialogArgs.fromSavedStateHandle(savedStateHandle).initial
    private var selectedCategories = mutableListOf<Category>()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    override fun loadData() {
        launchRequest {
            val categories = contentfulClient.fetchCategories()
            val categoryIds = categories.map { it.id }
            val filteredCategory = initialCategories.filter { categoryIds.contains(it.id) }
            selectedCategories.addAll(filteredCategory)
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    canLoadMore = false,
                    allFilters = categories,
                    selectedFilters = selectedCategories
                )
            )
        }
    }

    override fun handleEvent(event: CategoryFilterEvent) {
        when (event) {
            CategoryFilterEvent.CancelClick -> notifyEffectChanged(CategoryFilterEffect.ShowPrevScreen)
            is CategoryFilterEvent.CategoryClick -> if (selectedCategories.contains(event.category)) removeCategory(
                category = event.category
            ) else addCategory(event.category)
            CategoryFilterEvent.SearchClick -> notifyEffectChanged(
                CategoryFilterEffect.SendNewSelectedScreen(selectedCategories)
            )
        }
    }

    private fun addCategory(category: Category) {
        selectedCategories.add(category)
        notifyStateChange(currentState.copy(selectedFilters = selectedCategories.toList()))
    }

    private fun removeCategory(category: Category) {
        selectedCategories.remove(category)
        notifyStateChange(currentState.copy(selectedFilters = selectedCategories.toList()))
    }
}