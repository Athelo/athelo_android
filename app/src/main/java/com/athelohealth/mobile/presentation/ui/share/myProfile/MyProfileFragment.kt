package com.athelohealth.mobile.presentation.ui.share.myProfile

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToAuthorization
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToDeeplink
import com.athelohealth.mobile.utils.routeToEditProfile
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