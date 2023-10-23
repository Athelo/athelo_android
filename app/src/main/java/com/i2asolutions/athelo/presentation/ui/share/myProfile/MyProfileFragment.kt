package com.i2asolutions.athelo.presentation.ui.share.myProfile

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToAuthorization
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToDeeplink
import com.i2asolutions.athelo.utils.routeToEditProfile
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyProfileFragment : BaseComposeFragment<MyProfileViewModel>() {
    override val composeContent: @Composable () -> Unit = {
        MyProfileScreen(viewModel = viewModel)
    }
    override val viewModel: MyProfileViewModel by viewModels()

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when(it){
                MyProfileEffect.ShowEditProfileScreen -> routeToEditProfile(false)
                MyProfileEffect.ShowPrevScreen -> routeToBackScreen()
                is MyProfileEffect.ShowScreenFromDeeplink -> routeToDeeplink(it.deeplink)
                MyProfileEffect.ShowAuthorizationScreen -> routeToAuthorization()
            }
        }
    }
}