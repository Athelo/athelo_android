package com.i2asolutions.athelo.presentation.ui.askAthelo

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.feedback.FeedbackScreenType
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToFeedback
import com.i2asolutions.athelo.utils.routeToWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AskAtheloFragment : BaseComposeFragment<AskAtheloViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        AskAtheloScreen(viewModel = viewModel)
    }
    override val viewModel: AskAtheloViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                AskAtheloEffect.ShowFeedbackScreen -> routeToFeedback(FeedbackScreenType.AskAthelo)
                AskAtheloEffect.ShowPrevScreen -> routeToBackScreen()
                is AskAtheloEffect.OpenLinkScreen -> routeToWebView(it.url)
            }
        }
    }
}