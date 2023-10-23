package com.i2asolutions.athelo.presentation.ui.share.news

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.model.news.Category
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.presentation.ui.mainActivity.MainActivity
import com.i2asolutions.athelo.utils.routeToCategoryFilterPicker
import com.i2asolutions.athelo.utils.routeToMyProfile
import com.i2asolutions.athelo.utils.routeToNewsDetail
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
                is NewsEffect.OpenContentfulNewsDetailScreen -> routeToNewsDetail(effect.newsId)
            }
        }
    }
}