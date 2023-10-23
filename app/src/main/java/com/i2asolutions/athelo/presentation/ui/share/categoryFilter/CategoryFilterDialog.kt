package com.i2asolutions.athelo.presentation.ui.share.categoryFilter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.createComposeContainerView
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.news.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFilterDialog : BottomSheetDialogFragment() {
    private val viewModel: CategoryFilterViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.FullscreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeContainerView(backgroundColorRes = android.R.color.transparent) {
        FilterScreen(viewModel = viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadData()
    }

    private fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                is CategoryFilterEffect.SendNewSelectedScreen -> {
                    sendDataBack(it.selectedCategories)
                    dismissAllowingStateLoss()
                }
                CategoryFilterEffect.ShowPrevScreen -> dismissAllowingStateLoss()
            }
        }
    }

    private fun sendDataBack(selected: List<Category>) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "selected_categories",
            selected
        )
    }
}