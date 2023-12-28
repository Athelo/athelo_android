package com.athelohealth.mobile.presentation.ui.share.exercise

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToLostCaregiverAccess
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExerciseFragment : BaseComposeFragment<ExerciseViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ExerciseScreen(viewModel = viewModel)
    }
    override val viewModel: ExerciseViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                ExerciseEffect.GoBack -> routeToBackScreen()
                ExerciseEffect.ShowLostCaregiverScreen -> routeToLostCaregiverAccess()
            }
        }
    }
}