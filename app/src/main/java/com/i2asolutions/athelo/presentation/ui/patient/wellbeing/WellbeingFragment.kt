package com.i2asolutions.athelo.presentation.ui.patient.wellbeing

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.presentation.ui.mainActivity.MainActivity
import com.i2asolutions.athelo.utils.routeToAddSymptom
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToInfoSymptom
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