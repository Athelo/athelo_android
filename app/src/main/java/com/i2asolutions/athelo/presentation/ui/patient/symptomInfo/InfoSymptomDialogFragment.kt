package com.i2asolutions.athelo.presentation.ui.patient.symptomInfo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.createComposeContainerView
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToRecommendationSymptom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoSymptomDialogFragment : BottomSheetDialogFragment() {
    private val viewModel: InfoSymptomViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.FullscreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createComposeContainerView(backgroundColorRes = android.R.color.transparent) {
        InfoSymptomScreen(viewModel = viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        viewModel.loadData()
    }

    private fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                InfoSymptomEffect.ShowPrevScreen -> routeToBackScreen()
                is InfoSymptomEffect.ShowRecommendationScreen -> routeToRecommendationSymptom(effect.symptomId)
            }
        }
    }
}