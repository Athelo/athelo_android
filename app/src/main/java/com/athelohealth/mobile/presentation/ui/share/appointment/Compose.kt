@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)

package com.athelohealth.mobile.presentation.ui.share.appointment

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.news.News
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.DropdownMenu
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.athelohealth.mobile.presentation.ui.share.news.NewsEvent
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.dividerColor
import com.athelohealth.mobile.presentation.ui.theme.fonts
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.lightGreen
import com.athelohealth.mobile.presentation.ui.theme.lightOlivaceous
import com.athelohealth.mobile.presentation.ui.theme.purple
import com.athelohealth.mobile.presentation.ui.theme.red
import com.athelohealth.mobile.presentation.ui.theme.subtitle
import com.athelohealth.mobile.presentation.ui.theme.typography
import com.athelohealth.mobile.presentation.ui.theme.white
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.exp

@Composable
fun AppointmentScreen(viewModel: AppointmentViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
        content = Content(state = viewState, viewModel = viewModel) {
            viewModel.handleEvent(it)
        }
    )

}


@Composable
private fun Content(
    state: State<AppointmentViewState>,
    viewModel: AppointmentViewModel,
    handleEvent: (AppointmentEvent) -> Unit = {}
): @Composable (BoxScope.() -> Unit) = {
    Column(modifier = Modifier.fillMaxSize()) {
        if (viewModel.isAppointmentListEmpty.value == true) {
            Box(modifier = Modifier.weight(1f)) {
                Box {
                    InitToolBar(state = state, handleEvent = handleEvent)
                    // add condition for Scheduled appointment is available or not.
                    NoAppointments()
                }
            }
        } else {
            InitToolBar(state = state, handleEvent = handleEvent)
        }

        MainButton(
            textId = R.string.schedule_my_appointment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            onClick = {
                handleEvent(AppointmentEvent.ScheduleMyAppointmentClick)
            },
            background = purple
        )

        // Scheduled appointment list UI
        AppointmentList(viewModel)
    }
}

@Composable
fun InitToolBar(state: State<AppointmentViewState>, handleEvent: (AppointmentEvent) -> Unit = {}) {
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
            contentPadding = PaddingValues(bottom = 10.dp)
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
            items(2) { news ->
                /* NoImageNewsCell(
                     news = News(
                         id = 1,
                         isFavourite = false,
                         name = "Test name with maybe two lines of text?",
                         description = LoremIpsum(29).values.joinToString(" "),
                         categories = listOf("Cat 1", "Cat 2"),
                         image = com.athelohealth.mobile.presentation.model.base.Image(
                             image250250 = "https://placekitten.com/250/250"
                         )
                     ), modifier = Modifier, handleEvent = {}
                 )*/


                ScheduledAppointmentCell(
                    "Ave Calvar", "Car Navigator", onEditClicked = {}
                )

                //         }
            }
        }
    }

}

@Composable
fun NoAppointments() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    )
    {
        val painter = painterResource(id = R.drawable.appointment_top)
        Image(
            painter = painter,
            contentDescription = "top_background",
            Modifier
                .weight(1f, fill = false)
                //.aspectRatio((painter.intrinsicSize.height + 100) / 1280)
                .fillMaxWidth()
                .fillMaxHeight(1380f),
            contentScale = ContentScale.Crop
        )

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
    }
}


@Composable
private fun NoImageNewsCell(news: News, modifier: Modifier, handleEvent: (NewsEvent) -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable { handleEvent(NewsEvent.NewsItemClick(news)) },
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                news.categories.forEach { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(lightGreen.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
            Text(
                text = news.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = news.description,
                style = MaterialTheme.typography.subtitle.copy(fontWeight = FontWeight.Normal),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ScheduledAppointmentCell(
    name: String,
    hobby: String,
    onEditClicked: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(all = 10.dp),
        colors = CardDefaults.cardColors(background),
        elevation = CardDefaults.cardElevation(8.dp),
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

            Box(modifier = Modifier
                .weight(0.5f)
                .wrapContentSize(Alignment.TopStart)
                .clip(shape = RoundedCornerShape(8.dp))) {
                IconButton(
                    onClick = {
                        onEditClicked.invoke()
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

                DropDownMenu(expanded = expanded) {
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
fun DropDownMenu(expanded: Boolean, onStateChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }


    DropdownMenu(
        modifier = Modifier.background(background),
        expanded = expanded,
        onDismissRequest = { onStateChange.invoke(false) }
    ) {
        DropdownMenuItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = {
                Text(text = "Join Appointment",
                    color = darkPurple)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_join_appointment),
                    contentDescription = "Join appointment icon",
                    tint = darkPurple)
            },
            onClick = {
                // TODO: Show Alert dialog
                Toast.makeText(context, "Join appointment clicked!", Toast.LENGTH_SHORT).show()
                onStateChange.invoke(false)
                openDialog.value = true
            }
        )

        Divider(modifier = Modifier.padding(horizontal = 16.dp), color = dividerColor)

        DropdownMenuItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = {
                Text(text = "Cancel", color = red)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_cancel_appointment),
                    contentDescription = "Cancel appointment icon",
                    tint = red)
            },
            onClick = {
                // TODO: Show Alert dialog
                Toast.makeText(context, "Cancel appointment clicked!", Toast.LENGTH_SHORT).show()
                onStateChange.invoke(false)
                openDialog.value = true
            }
        )
    }

    if(openDialog.value) {
        CreateCustomAlertDialog(openDialog)
    }
}

@Composable
fun CreateCustomAlertDialog(openDialog: MutableState<Boolean>) {

    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        shape = RoundedCornerShape(16.dp),
        title = {
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(56.dp)
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.FillHeight
            )
        },
        text = {
               Column(modifier = Modifier.fillMaxWidth(),
                   horizontalAlignment = Alignment.CenterHorizontally) {
                   Text(text = "Cancel appointment",
                       style = typography.headlineSmall.copy(color = darkPurple))
                   Text(text = "Are you sure you want to cancel your appointment?",
                       style = typography.bodyMedium.copy(color = darkPurple),
                       textAlign = TextAlign.Center)
               }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "No",
                    color = lightOlivaceous,
                    modifier = Modifier.weight(1f)
                        .border(
                            width = 1.dp, color = darkPurple, shape = RoundedCornerShape(24.dp)
                        )
                        .height(48.dp)
                        .clickable {
                            openDialog.value = false
                        },
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Yes",
                    color = white,
                    modifier = Modifier.weight(1f).clip(shape = RoundedCornerShape(24.dp)).background(red).height(48.dp)
                        .clickable {
                            openDialog.value = false
                        },
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                )
            }
        })
}

@Preview
@Composable
fun previewMessage() {
//    val viewModel: AppointmentViewModel = viewModel()
//    val state = viewModel.viewState.collectAsState()
//    Content(state = state, viewModel = viewModel) {}
    //AppointmentScreen(viewModel = viewModel)

    ScheduledAppointmentCell(
        "Ave Calvar",
        "Car Nevogator"
    ) {}
}
