package com.athelohealth.mobile.presentation.ui.caregiver.myWard

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.onEachCollect
import com.athelohealth.mobile.presentation.ui.base.BaseComposeFragment
import com.athelohealth.mobile.utils.routeToBackScreen
import com.athelohealth.mobile.utils.routeToChat
import com.athelohealth.mobile.utils.routeToInvitationCode
import com.athelohealth.mobile.utils.routeToSelectRole
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWardFragment : BaseComposeFragment<MyWardViewModel>() {

    override val viewModel: MyWardViewModel by viewModels()

    override val composeContent: @Composable () -> Unit = {
        MyWardScreen(viewModel)
    }

    override fun setupObservers() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) { effect ->
            when (effect) {
                is MyWardEffect.ShowPatientConversationScreen -> routeToChat(
                    effect.conversationId,
                    false
                )
                is MyWardEffect.ShowDeletePatientScreen -> showAlertDialog(
                    title = getString(R.string.Confirmation),
                    message = getString(R.string.confirmation_delete_ward),
                    positiveButtonText = getString(R.string.Delete),
                    positiveButtonListener = {
                        viewModel.handleEvent(MyWardEvent.DeleteWardConfirmationClick(effect.patient))
                    },
                    negativeButtonText = getString(R.string.Cancel),
                    negativeButtonListener = {

                    }
                )
                MyWardEffect.ShowPrevScreen -> routeToBackScreen()
                MyWardEffect.ShowInvitationScreen -> routeToInvitationCode()
                MyWardEffect.ShowSelectRoleScreen -> routeToSelectRole(true)
            }
        }
    }
}