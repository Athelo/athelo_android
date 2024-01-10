package com.athelohealth.mobile.presentation.ui.share.categoryFilter

import androidx.lifecycle.SavedStateHandle
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.ui.base.BaseViewModel
import com.athelohealth.mobile.presentation.ui.share.authorization.signInWithEmail.SignInWithEmailViewState
import com.athelohealth.mobile.useCase.news.LoadNewsCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryFilterViewModel @Inject constructor(
    private val loadCategories: LoadNewsCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<CategoryFilterEvent, CategoryFilterEffect, CategoryFilterViewState>(CategoryFilterViewState()) {
    private val initialCategories =
        CategoryFilterDialogArgs.fromSavedStateHandle(savedStateHandle).initial
    private val selectedCategories = mutableListOf<Category>()

    override fun pauseLoadingState() { notifyStateChange(currentState.copy(isLoading = false)) }

    init {
        if (initialCategories.isNotEmpty()) {
            selectedCategories.addAll(initialCategories)
        }
    }

    override fun loadData() {
        notifyStateChange(currentState.copy(isLoading = false))
        launchRequest {
            val categories = loadCategories()
            notifyStateChange(
                currentState.copy(
                    isLoading = false,
                    canLoadMore = !categories.next.isNullOrBlank(),
                    allFilters = categories.result,
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