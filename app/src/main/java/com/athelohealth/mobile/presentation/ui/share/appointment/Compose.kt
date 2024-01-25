@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.athelohealth.mobile.presentation.ui.share.appointment

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.news.News
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.athelohealth.mobile.presentation.ui.share.news.NewsEvent
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.fonts
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.lightGreen
import com.athelohealth.mobile.presentation.ui.theme.purple
import com.athelohealth.mobile.presentation.ui.theme.subtitle
import com.athelohealth.mobile.presentation.ui.theme.white
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun AppointmentScreen(viewModel: AppointmentViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
        content = Content(state = viewState,viewModel=viewModel) {
            viewModel.handleEvent(it)
        }
    )

}


@Composable
private fun Content(
    state: State<AppointmentViewState>,
    viewModel: AppointmentViewModel,
    handleEvent: (AppointmentEvent) -> Unit = {}
): @Composable (BoxScope.() -> Unit) =
    {
        Column {
            Box(modifier = Modifier.weight(1f)) {
                Box {
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

                // add condition for Scheduled appointment is available or not.
                noAppointment()

            }

            MainButton(
                textId = R.string.schedule_my_appointment,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                onClick = {
                    handleEvent(AppointmentEvent.ScheduleMyAppointmentClick)
                },
                background = purple)

            // Scheduled appointment list UI
            // appointmntList(viewModel)

        }
    }


@Composable
fun appointmntList(viewModel : AppointmentViewModel) {
    val lazyState = rememberLazyListState()
    LaunchedEffect(key1 = null) {
        lazyState.scrollToItem(0)
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.loadData() }) {
        LazyColumn(
            modifier = Modifier,
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
                items(5) { news ->
                    NoImageNewsCell(
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
                    )
       //         }
            }
        }
    }

}

@Preview
@Composable
fun previewMessage() {
    //Content(state = viewModel.viewState.collectAsState())
}


@Composable
fun noAppointment(){
    Column {
        Box {
            Image(
                painter = painterResource(id = R.drawable.appointment_top),
                contentDescription = "top_background",
                Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
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
            text = stringResource(R.string.appointment_not_scheduled_msg)   ,
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
