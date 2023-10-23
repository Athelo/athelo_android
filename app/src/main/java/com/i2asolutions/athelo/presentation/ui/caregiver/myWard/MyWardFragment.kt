package com.i2asolutions.athelo.presentation.ui.caregiver.myWard

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.base.BaseComposeFragment
import com.i2asolutions.athelo.utils.routeToBackScreen
import com.i2asolutions.athelo.utils.routeToChat
import com.i2asolutions.athelo.utils.routeToInvitationCode
import com.i2asolutions.athelo.utils.routeToSelectRole
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