package com.athelohealth.mobile.presentation.ui.share.feedback

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
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