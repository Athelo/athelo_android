@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)

package com.i2asolutions.athelo.presentation.ui.patient.symptomChronology

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.health.SymptomChronology
import com.i2asolutions.athelo.presentation.model.health.SymptomChronologyHeader
import com.i2asolutions.athelo.presentation.model.health.SymptomChronologyItem
import com.i2asolutions.athelo.presentation.model.home.Feelings
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.CircleProgress
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithNameBack
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.createMockSymptoms
import java.util.*

@Composable
fun SymptomChronologyScreen(viewModel: SymptomChronologyViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            items = state.items,
            isLoading = { false },
            canLoadMore = { state.canLoadMore })
    }
}

@Composable
private fun Content(
    handleEvent: (SymptomChronologyEvent) -> Unit,
    items: List<SymptomChronologyItem>,
    isLoading: () -> Boolean,
    canLoadMore: () -> Boolean
) {
    Column(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        ToolbarWithNameBack(
            backClick = { handleEvent(SymptomChronologyEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Symptom_Chronology)
        )
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading()),
            onRefresh = { handleEvent(SymptomChronologyEvent.LoadFirstPage) }) {
            LazyColumn {
                items(items = items, key = { item -> item.id }) { item ->
                    when (item) {
                        is SymptomChronology -> {
                            ChronologyCell(chronology = item)
                        }
                        is SymptomChronologyHeader -> {
                            HeaderCell(item)
                        }
                    }
                }
                if (canLoadMore())
                    item {
                        Pagination(handleEvent = handleEvent)
                    }
                item {
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChronologyCell(chronology: SymptomChronology) {
    Row {
        DateLayout(
            day = chronology.day.day,
            month = chronology.day.month.shortName,
            week = chronology.day.shortWeekName
        )
        SymptomCell(names = chronology.symptomNames, feelings = chronology.feeling)
    }
}

@Composable
private fun HeaderCell(chronology: SymptomChronologyHeader) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .padding(top = 12.dp)
        .fillMaxWidth()) {
        Text(
            text = chronology.header,
            style = MaterialTheme.typography.subtitle.copy(color = darkPurple)
        )
    }
}

@Composable
fun SymptomCell(names: String, feelings: Feelings?) {
    Card(
        modifier = Modifier
            .padding(end = 16.dp)
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.Symptom),
                    style = MaterialTheme.typography.subtitle.copy(
                        color = gray,
                        textAlign = TextAlign.Start
                    ),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                )
                if (feelings != null)
                    Image(painter = painterResource(id = feelings.icon), contentDescription = null)
            }
            Text(
                text = names,
                style = MaterialTheme.typography.body1.copy(
                    color = purple,
                    textAlign = TextAlign.Start
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Suppress("DEPRECATION")
@Composable
private fun DateLayout(day: Int, month: String, week: String) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .padding(start = 16.dp, end = 8.dp)
            .widthIn(min = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$day",
            style = MaterialTheme.typography.headline20.copy(
                color = purple,
                textAlign = TextAlign.Center,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            textAlign = TextAlign.Center,
        )

        Text(
            text = month,
            style = MaterialTheme.typography.subHeading.copy(
                color = purple,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )

        Text(
            text = week,
            style = MaterialTheme.typography.body1.copy(
                color = gray,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )

    }
}


@Composable
private fun Pagination(handleEvent: (SymptomChronologyEvent) -> Unit) {
    Box(
        Modifier
            .requiredHeight(80.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(key1 = true) {
            handleEvent(SymptomChronologyEvent.LoadNextPage)
        }
        CircleProgress(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun ContentPreview() {
    Box(modifier = Modifier.background(background)) {
        Content(
            handleEvent = {},
            items = listOf(SymptomChronology(createMockSymptoms(10), Feelings.Sad, Date())),
            { false }, { false })
    }
}