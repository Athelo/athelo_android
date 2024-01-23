@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.appointment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.athelohealth.mobile.presentation.ui.share.news.NewsEvent

@Composable
fun AppointmentScreen(viewModel: AppointmentViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
        content = Content(state = viewState)

    )

}

@Composable
private fun Content(
    state: State<AppointmentViewState>,
    handleEvent: (NewsEvent) -> Unit = {}
): @Composable (BoxScope.() -> Unit) =
    {

            Box {

                ToolbarWithMenuAndMyProfile(
                    userAvatar = state.value.user.photo?.image5050,
                    userDisplayName = state.value.user.displayName ?: "",
                    menuClick = {
                        handleEvent(NewsEvent.MenuClick)
                    },
                    avatarClick = {
                        handleEvent(NewsEvent.MyProfileClick)
                    })
            }
            Box {
                Image(
                    painter = painterResource(id = R.drawable.appointment_top),
                    contentDescription = "top_background",
                    Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

    }


@Preview
@Composable
fun previewMessage() {
    AppointmentScreen(viewModel = AppointmentViewModel())
}