package com.athelohealth.mobile.presentation.ui.share.community

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithMenuAndMyProfile

@Composable
fun CommunityScreen(viewModel: CommunityViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        includeStatusBarPadding = false
    ) {
        ToolbarWithMenuAndMyProfile(
            userDisplayName = viewState.value.currentUser.displayName ?: "",
            userAvatar = viewState.value.currentUser.photo?.image5050,
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
