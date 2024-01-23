package com.athelohealth.mobile.presentation.ui.share.appointment

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.presentation.ui.share.news.ContentfulNewsScreen

class AppointmentFragment: BaseComposeFragment<AppointmentViewModel>() {
    override val composeContent: @Composable () -> Unit=
    {
        AppointmentScreen(viewModel = viewModel)
    }

    override val viewModel: AppointmentViewModel by viewModels()


    override fun setupObservers() {

    }
}