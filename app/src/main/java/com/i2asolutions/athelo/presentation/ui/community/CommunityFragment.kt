package com.i2asolutions.athelo.presentation.ui.community

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToChatList
import com.i2asolutions.athelo.utils.routeToMyProfile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseComposeFragment<CommunityViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        CommunityScreen(viewModel = viewModel)
    }
    override val viewModel: CommunityViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                CommunityEffect.ShowMenuScreen -> openMenu()
                CommunityEffect.ShowMyProfileScreen -> routeToMyProfile()
                CommunityEffect.ShowChatsScreen -> routeToChatList()
            }
        }
    }
}