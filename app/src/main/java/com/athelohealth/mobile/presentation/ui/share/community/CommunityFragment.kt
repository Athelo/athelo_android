package com.athelohealth.mobile.presentation.ui.share.community

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToChatList
import com.athelohealth.mobile.utils.routeToMyProfile
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