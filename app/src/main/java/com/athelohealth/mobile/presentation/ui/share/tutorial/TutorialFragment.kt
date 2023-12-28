package com.athelohealth.mobile.presentation.ui.share.tutorial

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToAuthorization
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