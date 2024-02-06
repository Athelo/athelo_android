@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package com.athelohealth.mobile.presentation.ui.share.appointment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BaseEvent
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.DropdownMenu
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.dividerColor
import com.athelohealth.mobile.presentation.ui.theme.fonts
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.lightOlivaceous
import com.athelohealth.mobile.presentation.ui.theme.purple
import com.athelohealth.mobile.presentation.ui.theme.red
import com.athelohealth.mobile.presentation.ui.theme.typography
import com.athelohealth.mobile.presentation.ui.theme.white
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun AppointmentScreen(viewModel: AppointmentViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.statusBarsPadding(),
        includeStatusBarPadding = false
    ) {
        if (viewModel.isAppointmentListEmpty.value == true) {
            EmptyAppointmentView(
                state = viewState,
                handleEvent = viewModel::handleEvent
            )
        } else {
            ScheduledAppointmentList(
                state = viewState,
                handleEvent = viewModel::handleEvent,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun EmptyAppointmentView(
    state: State<AppointmentViewState>,
    handleEvent: (AppointmentEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxHeight()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box {
            val painter = painterResource(id = R.drawable.ic_appointment_top)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(painter.intrinsicSize.width / (painter.intrinsicSize.height * 0.85f)),
                contentScale = ContentScale.FillBounds
            )
            InitToolBar(state = state, handleEvent = handleEvent)
        }

        Text(
            text = stringResource(id = R.string.welcome_to_the_appointments),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp,
            color = darkPurple,
            fontFamily = fonts,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .padding(horizontal = 15.dp)
        )

        Text(
            text = stringResource(R.string.appointment_not_scheduled_msg),
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
            color = gray,
            fontFamily = fonts,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(horizontal = 15.dp)
        )

        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textId = R.string.schedule_new_appointment,
            background = purple,
            onClick = {
                handleEvent(AppointmentEvent.ScheduleMyAppointmentClick)
            }
        )
    }
}

@Composable
fun ScheduledAppointmentList(
    state: State<AppointmentViewState>,
    handleEvent: (AppointmentEvent) -> Unit,
    viewModel: AppointmentViewModel
) {
    Column {
        InitToolBar(state = state, handleEvent = handleEvent)

        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            textId = R.string.schedule_new_appointment,
            background = purple,
            onClick = {
                handleEvent(AppointmentEvent.ScheduleMyAppointmentClick)
            }
        )

        AppointmentList(viewModel)
    }
}

@Composable
fun InitToolBar(state: State<AppointmentViewState>, handleEvent: (AppointmentEvent) -> Unit) {
    ToolbarWithMenuAndMyProfile(
        userAvatar = state.value.user.photo?.image5050,
        userDisplayName = state.value.user.displayName ?: "",
        menuClick = {
            handleEvent(AppointmentEvent.MenuClick)
        },
        avatarClick = {
            handleEvent(AppointmentEvent.MyProfileClick)
        })
}

@Composable
fun AppointmentList(viewModel: AppointmentViewModel) {
    val lazyState = rememberLazyListState()
    LaunchedEffect(key1 = null) {
        lazyState.scrollToItem(0)
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.loadData() }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            state = lazyState,
            contentPadding = PaddingValues(bottom = 56.dp)
        ) {
            stickyHeader {
                /*         Box {
                             Text(
                                 text = "", modifier = Modifier
                                     .height(54.dp)
                                     .fillMaxWidth()
                                     .background(background)
                             )
                             SearchInputTextField(
                                 initialValue= viewModel.currentQuery(),
                                 modifier = Modifier
                                     .padding(horizontal = 16.dp)
                                     .padding(bottom = 24.dp, top = 24.dp),
                                 onChange = { search ->
                                     viewModel.updateNewsList(search.text)
                                 },
                                 keyboardActions = KeyboardActions(onSearch = {
                                     focusManager.clearFocus()
                                 }),
                                 hint = stringResource(id = R.string.Search)
                             )
                         }*/
            }
            /*        if (contentfulViewState.value.isEmpty()) {
                        item {
                            noAppointment()
                        }
                    } else {*/
            items(5) { _ ->
                ScheduledAppointmentCell(
                    name = "Ave Calvar",
                    hobby = "Car Navigator",
                    viewModel = viewModel
                )

                //         }
            }
        }
    }

}

@Composable
fun ScheduledAppointmentCell(
    name: String,
    hobby: String,
    viewModel: AppointmentViewModel
) {

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(all = 10.dp),
        colors = CardDefaults.cardColors(background),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user_avatar),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .aspectRatio(1f / 1f)
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(4f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = name,
                    style = typography.labelMedium.copy(
                        color = gray,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = hobby,
                    style = typography.bodyMedium.copy(
                        color = gray,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .wrapContentSize(Alignment.TopStart)
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = {
                        expanded = true
                    },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more_vertical),
                        contentDescription = stringResource(id = R.string.app_name)
                    )
                }

                DropDownMenu(expanded = expanded, viewModel) {
                    expanded = it
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = dividerColor,
            thickness = 2.dp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "17 JAN, 12:30 PM",
                color = lightOlivaceous,
                modifier = Modifier
                    .border(
                        width = 1.dp, color = lightOlivaceous, shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                fontSize = 12.sp
            )

            Text(
                text = "UTC-12:00",
                color = gray,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End,
                fontSize = 13.sp
            )
        }

    }
}


@Composable
fun DropDownMenu(
    expanded: Boolean,
    viewModel: AppointmentViewModel,
    onStateChange: (Boolean) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val shouldShowRescheduleDialog = remember { mutableStateOf(false) }

    DropdownMenu(
        modifier = Modifier
            .background(background)
            .padding(end = 16.dp)
            .fillMaxWidth(0.7f),
        expanded = expanded,
        onDismissRequest = {
            onStateChange.invoke(false)
        }
    ) {
        MyDropDownMenuItem(
            painterResource = painterResource(id = R.drawable.ic_join_appointment),
            tintColor = darkPurple,
            itemName = "Join appointment",
            onClick = {
                onStateChange.invoke(false)
                openDialog.value = false
            }
        )

        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = dividerColor)

        MyDropDownMenuItem(
            painterResource = painterResource(id = R.drawable.ic_cancel_appointment),
            tintColor = red,
            itemName = "Cancel",
            onClick = {
                onStateChange.invoke(false)
                openDialog.value = true
            }
        )
    }

    if (openDialog.value) {
        CancelAppointmentDialog(
            title = "Cancel appointment",
            message = "Are you sure you want to cancel your appointment?",
            negativeBtnText = "No",
            negativeBtnTextColor = darkPurple,
            positiveBtnText = "Yes",
            positiveBtnTextColor = white,
            positiveBtnBgColor = red,
            onDialogDismiss = {
                openDialog.value = false
            },
            onNegativeBtnClicked = {
                openDialog.value = false
            },
            onPositiveBtnClicked = {
                openDialog.value = false
                shouldShowRescheduleDialog.value = true
            }
        )
    }

    if (shouldShowRescheduleDialog.value) {
        CancelAppointmentDialog(
            title = "Re-schedule appointment",
            message = "Do you want to re-schedule your appointment?",
            negativeBtnText = "No",
            negativeBtnTextColor = darkPurple,
            positiveBtnText = "Yes",
            positiveBtnTextColor = white,
            positiveBtnBgColor = purple,
            onDialogDismiss = {
                shouldShowRescheduleDialog.value = false
            },
            onNegativeBtnClicked = {
                shouldShowRescheduleDialog.value = false
                viewModel.sendBaseEvent(BaseEvent.DisplaySuccess("Never mind! You can schedule new appointment!"))

            },
            onPositiveBtnClicked = {
                shouldShowRescheduleDialog.value = false
                viewModel.handleEvent(AppointmentEvent.ScheduleMyAppointmentClick)
            })
    }
}

@Composable
fun MyDropDownMenuItem(
    painterResource: Painter,
    tintColor: Color,
    itemName: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(all = 0.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "Dropdown menu icon",
                tint = tintColor,
                modifier = Modifier.padding(start = 20.dp)
            )
        },
        text = {
            Text(
                text = itemName,
                color = tintColor,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 24.dp)
            )
        },
        onClick = { onClick.invoke() }
    )
}

@Preview
@Composable
fun PreviewAppointmentScreen() {
    val viewModel: AppointmentViewModel = viewModel()
    val state = viewModel.viewState.collectAsState()

    EmptyAppointmentView(state = state, handleEvent = viewModel::handleEvent)

//    ScheduledAppointmentList(
//        state = state,
//        handleEvent = viewModel::handleEvent,
//        viewModel = viewModel
//    )
}
