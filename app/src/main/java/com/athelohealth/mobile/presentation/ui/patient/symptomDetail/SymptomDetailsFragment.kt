package com.athelohealth.mobile.presentation.ui.patient.symptomDetail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToSymptomChronologyScreen
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