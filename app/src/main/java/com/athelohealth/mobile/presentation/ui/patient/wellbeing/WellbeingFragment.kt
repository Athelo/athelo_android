package com.athelohealth.mobile.presentation.ui.patient.wellbeing

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.model.health.Symptom
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.presentation.ui.mainActivity.MainActivity
import com.athelohealth.mobile.utils.routeToAddSymptom
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToInfoSymptom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WellbeingFragment : BaseComposeFragment<WellbeingViewModel>() {

    override val viewModel: WellbeingViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        WellbeingScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                WellbeingEffect.ShowPrevScreen -> routeToBackScreen()
                is WellbeingEffect.ShowSymptomChronologyScreen -> showToBeImplementedMessage()
                is WellbeingEffect.ShowAddSymptomScreen -> {
                    (requireActivity() as? MainActivity)?.setupSingleUseCallback<Symptom>("created_symptom") {
                        if (it != null) {
                            viewModel.handleEvent(WellbeingEvent.SymptomAdded(it))
                        }
                    }
                    routeToAddSymptom(effect.symptom, effect.day)
                }
                is WellbeingEffect.ShowInfoSymptomScreen -> routeToInfoSymptom(effect.symptom)
            }
        }
    }
}