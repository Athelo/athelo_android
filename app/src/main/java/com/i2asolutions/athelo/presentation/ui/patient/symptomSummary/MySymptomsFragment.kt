package com.i2asolutions.athelo.presentation.ui.patient.symptomSummary

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToSymptomDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MySymptomsFragment : BaseComposeFragment<MySymptomsViewModel>() {

    override val viewModel: MySymptomsViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MySymptomsScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                MySymptomsEffect.ShowPrevScreen -> routeToBackScreen()
                is MySymptomsEffect.ShowSymptomDetail -> routeToSymptomDetail(effect.symptomId)
            }
        }
    }
}