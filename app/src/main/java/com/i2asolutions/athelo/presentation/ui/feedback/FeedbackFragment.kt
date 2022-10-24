package com.i2asolutions.athelo.presentation.ui.feedback

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : BaseComposeFragment<FeedbackViewModel>() {
    override val viewModel: FeedbackViewModel by viewModels()
    override val composeContent: @Composable () -> Unit = {
        FeedbackScreen(viewModel = viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                FeedbackEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }

}