package com.i2asolutions.athelo.presentation.ui.exercise

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
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
            }
        }
    }
}