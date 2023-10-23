package com.i2asolutions.athelo.presentation.ui.share.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithMenuAndMyProfile

@Composable
fun CommunityScreen(viewModel: CommunityViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        includeStatusBarPadding = false
    ) {
        ToolbarWithMenuAndMyProfile(
            userDisplayName = state.value.currentUser.displayName ?: "",
            userAvatar = state.value.currentUser.photo?.image5050,
            menuClick = {
                viewModel.handleEvent(CommunityEvent.MenuClick)
            },
            avatarClick = {
                viewModel.handleEvent(CommunityEvent.MyProfileClick)
            })

        MainButton(
            textId = R.string.Chat_List,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(
                    Alignment.Center
                ),
            onClick = {
                viewModel.handleEvent(CommunityEvent.ChatListClick)
            })
    }
}
