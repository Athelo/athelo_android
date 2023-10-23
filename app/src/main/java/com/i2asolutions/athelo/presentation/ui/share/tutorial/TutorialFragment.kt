package com.i2asolutions.athelo.presentation.ui.share.tutorial

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToAuthorization
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialFragment : BaseComposeFragment<TutorialViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        TutorialScreen(viewModel = viewModel)
    }
    override val viewModel: TutorialViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                TutorialEffect.CloseApp -> requireActivity().finishAffinity()
                TutorialEffect.ShowAuthorization -> routeToAuthorization()
            }
        }
    }
}