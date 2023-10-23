@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.patient.symptomSummary

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.model.health.SymptomSummary
import com.i2asolutions.athelo.presentation.model.mySymptoms.MySymptomsListType
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.background
import com.i2asolutions.athelo.presentation.ui.theme.button
import com.i2asolutions.athelo.presentation.ui.theme.gray
import com.i2asolutions.athelo.presentation.ui.theme.white
import com.i2asolutions.athelo.utils.createMockImage
import com.i2asolutions.athelo.utils.createMockSymptomsSummary

@Composable
fun MySymptomsScreen(viewModel: MySymptomsViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(
            handleEvent = viewModel::handleEvent,
            selectedTab = { state.selectedTab },
            data = { state.data },
            canLoadMore = { state.canLoadMore }
        )
    }
}

@Composable
private fun Content(
    handleEvent: (MySymptomsEvent) -> Unit,
    selectedTab: () -> MySymptomsListType,
    data: () -> List<SymptomSummary>,
    canLoadMore: () -> Boolean
) {
    Column {
        ToolbarWithNameBack(
            backClick = { handleEvent(MySymptomsEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.My_Symptoms)
        )
        RadioButtonGroup(
            selectionProvider = {
                if (selectedTab() == MySymptomsListType.MostUsed) 1 else 0
            },
            modifier = Modifier
                .padding(horizontal = 16.dp),
            buttons = arrayOf(
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.All),
                    onClick = {
                        handleEvent(MySymptomsEvent.TabChanged(MySymptomsListType.All))
                    }
                ),
                RadioButton(
                    includePadding = false,
                    text = stringResource(id = R.string.Most_Used),
                    onClick = {
                        handleEvent(MySymptomsEvent.TabChanged(MySymptomsListType.MostUsed))
                    })
            )
        )
        LazyColumn {
            items(items = data(), key = { it.symptom.id }) { symptom ->
                SymptomCell(symptom = symptom.symptom, handleEvent = handleEvent)
            }
            if (canLoadMore()) {
                item {
                    Pagination(handleEvent = handleEvent)
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(30.dp)
                )
            }
        }
    }
}

@Composable
private fun SymptomCell(symptom: Symptom, handleEvent: (MySymptomsEvent) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(5.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                handleEvent(MySymptomsEvent.CellClick(symptom))
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageWithProgress(
                image = symptom.icon?.image,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(24.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = symptom.name,
                style = MaterialTheme.typography.button.copy(color = gray),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_gray),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun Pagination(handleEvent: (MySymptomsEvent) -> Unit) {
    Box(
        Modifier
            .requiredHeight(80.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(key1 = true) {
            handleEvent(MySymptomsEvent.LoadNextPage)
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
fun SymptomCellPrev() {
    SymptomCell(symptom = Symptom(
        id = 1, name = "Test",
        comment = "", description = "test", createMockImage(50), 1
    ),
        handleEvent = {})
}

@Preview
@Composable
fun ContentPrev() {
    Box(Modifier.background(background)) {
        Content(handleEvent = {}, selectedTab = { MySymptomsListType.All }, data = {
            createMockSymptomsSummary(20)
        }, canLoadMore = { true })
    }
}