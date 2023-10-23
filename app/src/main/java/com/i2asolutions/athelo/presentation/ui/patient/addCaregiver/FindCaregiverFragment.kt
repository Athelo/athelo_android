package com.i2asolutions.athelo.presentation.ui.patient.addCaregiver

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindCaregiverFragment : BaseComposeFragment<FindCaregiverViewModel>() {

    override val viewModel: FindCaregiverViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        FindCaregiverScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                FindCaregiverEffect.ShowPrevScreen -> {
                    routeToBackScreen()
                }
            }
        }
    }
}