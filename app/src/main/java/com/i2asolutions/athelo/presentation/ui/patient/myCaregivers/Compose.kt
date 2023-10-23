@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.patient.myCaregivers

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.caregiver.Caregiver
import com.i2asolutions.athelo.presentation.model.caregiver.Invitation
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.*

@Composable
fun MyCaregiversScreen(viewModel: MyCaregiversViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            caregiversProvider = state.caregivers,
            selectedTabProvider = { state.selectedType },
            invitationsProvider = state.invitations,
            canLoadMoreProvider = state.canLoadMore,
        )
        CaregiverOptionPopup(
            handleEvent = viewModel::handleEvent,
            caregiver = state.showCaregiverMoreOption
        )
        val deletePatient = state.showCaregiverDeleteConfirmation
        if (deletePatient != null) {
            ConfirmDeletePopup(viewModel::handleEvent, deletePatient)
        }
        val deleteInvitation = state.showInvitationDeleteConfirmation
        if (deleteInvitation != null) {
            ConfirmDeleteInvitationPopup(
                handleEvent = viewModel::handleEvent,
                invitation = deleteInvitation
            )
        }
    }
}

@Composable
private fun Content(
    handleEvent: (MyCaregiversEvent) -> Unit,
    caregiversProvider: List<Caregiver>,
    selectedTabProvider: () -> MyCaregiverListType,
    invitationsProvider: List<Invitation>,
    canLoadMoreProvider: Boolean,
) {
    Column {
        ScreenNameToolbar(
            leftButton = {
                BackButton {
                    handleEvent(MyCaregiversEvent.BackButtonClick)
                }
            },
            screenName = stringResource(id = R.string.My_Caregivers),
            rightButton = {
                Image(
                    painter = painterResource(id = R.drawable.plus_small),
                    contentDescription = "Add Caregiver",
                    modifier = Modifier
                        .size(56.dp)
                        .clickable(onClick = { handleEvent(MyCaregiversEvent.AddCaregiverButtonClick) }),
                    contentScale = ContentScale.Inside
                )
            },
        )
        RadioButtonGroup(
            selectionProvider = {
                if (selectedTabProvider() == MyCaregiverListType.PendingInvitation) 1 else 0
            },
            modifier = Modifier
                .padding(horizontal = 16.dp),
            buttons = arrayOf(
                RadioButton(
                    text = stringResource(id = R.string.All),
                    onClick = {
                        handleEvent(MyCaregiversEvent.ListTypeButtonClick(MyCaregiverListType.MyCaregivers))
                    }
                ),
                RadioButton(
                    text = stringResource(id = R.string.Pending),
                    onClick = {
                        handleEvent(MyCaregiversEvent.ListTypeButtonClick(MyCaregiverListType.PendingInvitation))
                    })
            )
        )
        val lazyState = rememberLazyListState()
        LaunchedEffect(key1 = selectedTabProvider()) {
            lazyState.scrollToItem(0)
        }
        val manager = LocalConfiguration.current
        AnimatedVisibility(
            visible = selectedTabProvider() == MyCaregiverListType.MyCaregivers,
            enter = slideInHorizontally() + fadeIn(),
            exit = slideOutHorizontally() + fadeOut(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 24.dp, bottom = 53.dp),
                state = lazyState,
            ) {
                items(
                    items = caregiversProvider,
                    key = { caregiver -> caregiver.id }) { caregiver ->
                    CaregiverCell(handleEvent = handleEvent, caregiver = caregiver)
                }

                if (canLoadMoreProvider) {
                    item {
                        LoadMoreCell(handleEvent = handleEvent)
                    }
                }
            }
            if (caregiversProvider.isEmpty()) {
                EmptyCell(desc = stringResource(id = R.string.It_looks_like_you_dont_have_a_caregiver))
            }
        }
        AnimatedVisibility(
            visible = selectedTabProvider() == MyCaregiverListType.PendingInvitation,
            enter = slideInHorizontally(initialOffsetX = {
                manager.screenWidthDp + 50
            }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = {
                manager.screenWidthDp + 50
            }) + fadeOut(),
        ) {
            LazyColumn(
                contentPadding = PaddingValues(top = 24.dp, bottom = 53.dp),
                state = lazyState,
            ) {
                items(
                    items = invitationsProvider,
                    key = { invitation -> invitation.id }) { invitation ->
                    when (invitation) {
                        is Invitation.PendingInvitation -> {
                            PendingInvitationCell(
                                handleEvent = handleEvent,
                                invitation = invitation,
                            )
                        }
                        is Invitation.CanceledInvitation -> CancelledInvitationCell(
                            handleEvent = handleEvent,
                            invitation = invitation,
                        )
                        is Invitation.ConsumedInvitation -> ConsumedInvitationCell(
                            handleEvent = handleEvent,
                            invitation = invitation
                        )
                        is Invitation.ExpireInvitation -> ExpiredInvitationCell(
                            handleEvent = handleEvent,
                            invitation = invitation
                        )
                    }
                }
                if (canLoadMoreProvider) {
                    item {
                        LoadMoreCell(handleEvent = handleEvent)
                    }
                }
            }
            if (invitationsProvider.isEmpty()) {
                EmptyCell(desc = stringResource(id = R.string.It_looks_like_you_dont_have_any_pending_invitations))
            }
        }
    }
}

@Composable
private fun CaregiverCell(
    handleEvent: (MyCaregiversEvent) -> Unit,
    caregiver: Caregiver,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                /*.clickable {
                    handleEvent(MyWardEvent.PatientClick(patient))
                }*/
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            RoundedCornerAvatarImage(
                avatar = caregiver.photo?.image100100,
                displayName = caregiver.displayName,
                shape = RoundedCornerShape(20.dp)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = caregiver.displayName,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = caregiver.relation,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_more),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { handleEvent(MyCaregiversEvent.ShowMoreOptionClick(caregiver)) })
        }
    }
}

@Composable
private fun PendingInvitationCell(
    handleEvent: (MyCaregiversEvent) -> Unit,
    invitation: Invitation.PendingInvitation,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = invitation.name,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = invitation.email,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
            Image(
                painter = painterResource(id = R.drawable.ic_cancel_button),
                contentDescription = null,
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { handleEvent(MyCaregiversEvent.DeleteInvitationClick(invitation)) })
        }
    }
}

@Composable
private fun CancelledInvitationCell(
    handleEvent: (MyCaregiversEvent) -> Unit,
    invitation: Invitation.CanceledInvitation,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = invitation.name,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = invitation.email,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Composable
private fun ConsumedInvitationCell(
    handleEvent: (MyCaregiversEvent) -> Unit,
    invitation: Invitation.ConsumedInvitation,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = invitation.name,
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = invitation.email,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Composable
private fun ExpiredInvitationCell(
    handleEvent: (MyCaregiversEvent) -> Unit,
    invitation: Invitation.ExpireInvitation,
    bgShape: Shape = RoundedCornerShape(30.dp)
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = bgShape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(bgShape)
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "(Expired) ${invitation.name}",
                    style = MaterialTheme.typography.subHeading.copy(color = gray)
                )
                Text(
                    text = invitation.email,
                    style = MaterialTheme.typography.body1.copy(color = gray.copy(alpha = 0.8f))
                )
            }
        }
    }
}

@Composable
fun LoadMoreCell(handleEvent: (MyCaregiversEvent) -> Unit) {
    Box(
        Modifier
            .requiredHeight(80.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(key1 = true) {
            handleEvent(MyCaregiversEvent.LoadNextPage)
        }
        CircleProgress(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun CaregiverOptionPopup(handleEvent: (MyCaregiversEvent) -> Unit, caregiver: Caregiver?) {
    AnimatedVisibility(visible = caregiver != null, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(black.copy(alpha = 0.2f))
                .clickable(enabled = false) {

                },
            contentAlignment = Alignment.BottomCenter,
        ) {
            val targetH = LocalConfiguration.current.screenHeightDp
            AnimatedVisibility(
                visible = caregiver != null,
                enter = slideInVertically(
                    initialOffsetY = { targetH },
                    animationSpec = tween(delayMillis = 300, durationMillis = 800)
                ),
                exit = slideOutVertically(targetOffsetY = { targetH })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(background),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        OptionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (caregiver != null)
                                        handleEvent(MyCaregiversEvent.SendMessageClick(caregiver))
                                },
                            iconRes = R.drawable.ic_message,
                            textRes = R.string.Send_a_message,
                            paddingValues = PaddingValues(
                                start = 24.dp,
                                top = 24.dp,
                                end = 24.dp,
                                bottom = 12.dp
                            )
                        )
                        OptionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (caregiver != null)
                                        handleEvent(MyCaregiversEvent.DeleteCaregiverClick(caregiver))
                                },
                            iconRes = R.drawable.ic_trash_bin,
                            textRes = R.string.Delete_ward,
                            color = red,
                            paddingValues = PaddingValues(
                                start = 24.dp,
                                top = 12.dp,
                                end = 24.dp,
                                bottom = 24.dp
                            )
                        )
                    }

                    SecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                        text = R.string.Cancel,
                        onClick = {
                            if (caregiver != null) handleEvent(
                                MyCaregiversEvent.CancelClick
                            )
                        },
                        background = background,
                        border = darkPurple,
                        textColor = darkPurple
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyCell(desc: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.This_page_is_currently_empty),
            style = MaterialTheme.typography.headline20.copy(color = black),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = desc,
            style = MaterialTheme.typography.normalText.copy(color = darkPurple),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun OptionButton(
    modifier: Modifier,
    iconRes: Int,
    textRes: Int,
    color: Color = darkPurple,
    paddingValues: PaddingValues
) =
    Row(modifier) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = paddingValues.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding(),
                    end = 16.dp,
                )
                .size(24.dp),
            colorFilter = ColorFilter.tint(color, BlendMode.SrcAtop)
        )
        Text(
            modifier = Modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = paddingValues.calculateBottomPadding(),
                )
                .weight(1f),
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.button.copy(color),
        )
    }

@Composable
private fun BoxScope.ConfirmDeletePopup(
    handleEvent: (MyCaregiversEvent) -> Unit,
    caregiver: Caregiver,
) {
    DeletePopup(
        onConfirmClick = { handleEvent(MyCaregiversEvent.DeleteCaregiverConfirmationClick(caregiver)) },
        onCancelClick = { handleEvent(MyCaregiversEvent.CancelClick) },
        title = stringResource(id = R.string.Delete_a_Caregiver),
        description = stringResource(id = R.string.Confirm_Delete_Caregiver_Last_message)
    )
}

@Composable
private fun BoxScope.ConfirmDeleteInvitationPopup(
    handleEvent: (MyCaregiversEvent) -> Unit,
    invitation: Invitation,
) {
    DeletePopup(
        onConfirmClick = {
            handleEvent(
                MyCaregiversEvent.DeleteInvitationConfirmationClick(
                    invitation
                )
            )
        },
        onCancelClick = { handleEvent(MyCaregiversEvent.CancelClick) },
        title = stringResource(id = R.string.Cancel_Invitation),
        description = stringResource(id = R.string.Cancel_Invitation_message, invitation.name),
        confirmTextRes = R.string.Revoke
    )
}