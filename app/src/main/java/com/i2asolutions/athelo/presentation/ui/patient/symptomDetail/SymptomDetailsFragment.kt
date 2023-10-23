package com.i2asolutions.athelo.presentation.ui.patient.symptomDetail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToSymptomChronologyScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SymptomDetailsFragment : BaseComposeFragment<SymptomDetailsViewModel>() {

    override val viewModel: SymptomDetailsViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MySymptomDetailsScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                SymptomDetailsEffect.ShowChronologyScreen -> routeToSymptomChronologyScreen()
                SymptomDetailsEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}