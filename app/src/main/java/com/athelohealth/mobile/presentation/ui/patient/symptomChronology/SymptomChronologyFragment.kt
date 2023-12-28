package com.athelohealth.mobile.presentation.ui.patient.symptomChronology

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SymptomChronologyFragment : BaseComposeFragment<SymptomChronologyViewModel>() {

    override val viewModel: SymptomChronologyViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        SymptomChronologyScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                SymptomChronologyEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}