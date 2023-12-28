package com.athelohealth.mobile.presentation.ui.share.askAthelo

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.model.feedback.FeedbackScreenType
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToFeedback
import com.athelohealth.mobile.utils.routeToWebView
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