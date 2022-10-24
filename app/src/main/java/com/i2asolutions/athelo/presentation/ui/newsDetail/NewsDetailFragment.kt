package com.i2asolutions.athelo.presentation.ui.newsDetail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : BaseComposeFragment<NewsDetailViewModel>() {

    override val viewModel: NewsDetailViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        NewsDetailScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is NewsDetailEffect.OpenWebBrowser -> routeToWebView(effect.link)
                NewsDetailEffect.ShowPrevScreen -> routeToBackScreen()
            }
        }
    }
}