package com.athelohealth.mobile.presentation.ui.share.newsDetail

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsDetailFragment : BaseComposeFragment<NewsDetailViewModel>() {

    override val viewModel: NewsDetailViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        ContentfulNewsDetailScreen(viewModel)
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