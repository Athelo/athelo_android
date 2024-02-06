package com.athelohealth.mobile.presentation.ui.share.news

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.presentation.ui.mainActivity.MainActivity
import com.athelohealth.mobile.utils.navigateToInAppBrowser
import com.athelohealth.mobile.utils.routeToCategoryFilterPicker
import com.athelohealth.mobile.utils.routeToMyProfile
import com.athelohealth.mobile.utils.routeToNewsDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseComposeFragment<NewsViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        ContentfulNewsScreen(viewModel = viewModel)
    }
    override val viewModel: NewsViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                NewsEffect.ShowMenuScreen -> openMenu()
                is NewsEffect.OpenNewsDetailScreen -> routeToNewsDetail(effect.news.id.toString())
                is NewsEffect.ShowCategoryFilter -> {
                    (requireActivity() as? MainActivity)?.setupSingleUseCallback<List<Category>>("selected_categories") {
                        viewModel.handleEvent(NewsEvent.SearchCategoriesUpdate(it ?: emptyList()))
                    }
                    routeToCategoryFilterPicker(effect.initialSelection)
                }
                NewsEffect.ShowMyProfileScreen -> routeToMyProfile()
                is NewsEffect.OpenContentfulNewsDetailScreen -> {
                    if (effect.news.shouldOpenInBrowser && !effect.news.browserUrl.isNullOrEmpty()) {
                        navigateToInAppBrowser(effect.news.browserUrl)
                    } else {
                        routeToNewsDetail(effect.news.key, effect.isFavourite)
                    }
                }
            }
        }
    }
}