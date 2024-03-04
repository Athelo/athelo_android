package com.athelohealth.mobile.presentation.ui.patient.symptomSummary

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToSymptomChronologyScreen
import com.athelohealth.mobile.utils.routeToSymptomDetail
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
                MySymptomsEffect.ShowChronologyScreen -> routeToSymptomChronologyScreen()
                is MySymptomsEffect.ShowSymptomDetail -> routeToSymptomDetail(effect.symptomId)
            }
        }
    }
}