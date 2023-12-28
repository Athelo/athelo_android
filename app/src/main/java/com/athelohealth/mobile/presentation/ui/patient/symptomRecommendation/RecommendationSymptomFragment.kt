package com.athelohealth.mobile.presentation.ui.patient.symptomRecommendation

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
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