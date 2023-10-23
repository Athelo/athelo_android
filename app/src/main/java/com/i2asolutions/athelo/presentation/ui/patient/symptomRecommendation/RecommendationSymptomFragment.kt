package com.i2asolutions.athelo.presentation.ui.patient.symptomRecommendation

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendationSymptomFragment : BaseComposeFragment<RecommendationSymptomViewModel>() {

    override val viewModel: RecommendationSymptomViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        RecommendationSymptomScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                RecommendationSymptomEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}