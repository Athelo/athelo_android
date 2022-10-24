@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.flowlayout.FlowRow
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.model.home.Feelings
import com.i2asolutions.athelo.presentation.model.home.HomeItems
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithMenuAndMyProfile
import com.i2asolutions.athelo.presentation.ui.theme.*

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val state by viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Column {
            ToolbarWithMenuAndMyProfile(
                userAvatar = state.userAvatar,
                userDisplayName = state.displayName,
                menuClick = { viewModel.handleEvent(HomeEvent.MenuClick) },
                avatarClick = { viewModel.handleEvent(HomeEvent.MyProfileClick) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                contentPadding = PaddingValues(bottom = 30.dp)
            ) {
                item {
                    WelcomeText(
                        name = state.displayName,
                        Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 24.dp)
                    )
                }
                items(state.listItems) { item ->
                    when (item) {
                        is HomeItems.HeaderHome -> HeaderLabel(
                            text = item.message,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 24.dp)
                        )
                        is HomeItems.ButtonHome -> HomeButton(
                            item = item,
                            modifier = Modifier,
                            onItemClick = {
                                viewModel.handleEvent(event = HomeEvent.ItemClick(it))
                            }
                        )
                        is HomeItems.SymptomsHome -> SymptomsCell(
                            modifier = Modifier
                                .padding(
                                    horizontal = 16.dp
                                )
                                .padding(bottom = 24.dp),
                            symptoms = item.symptoms
                        )
                        is HomeItems.WellbeingHome -> WellbeingCell(
                            modifier = Modifier
                                .padding(
                                    horizontal = 16.dp
                                )
                                .padding(bottom = 24.dp),
                            feeling = item.wellbeing
                        )
                    }
                }
                item {
                    Box(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun WelcomeText(name: String, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(id = R.string.Welcome_format, name.split(" ").firstOrNull() ?: ""),
        style = MaterialTheme.typography.headline30.copy(color = purple)
    )
}

@Composable
private fun HeaderLabel(text: String, modifier: Modifier) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.subHeading.copy(color = gray),
    )
}

@Composable
private fun WellbeingCell(modifier: Modifier, feeling: Feelings) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(lightGreen.copy(alpha = 0.23f)),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.It_s_your_feelings_today),
                    style = MaterialTheme.typography.subHeading.copy(
                        color = black,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "(${feeling.feelingName})",
                    style = MaterialTheme.typography.subHeading.copy(
                        color = gray,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Image(
                painter = painterResource(id = feeling.icon),
                contentDescription = feeling.feelingName,
                modifier = Modifier.size(43.dp)
            )
        }
    }
}

@Composable
private fun SymptomsCell(modifier: Modifier, symptoms: List<Symptom>) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth(),
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 8.dp
    ) {
        symptoms.forEach { symptom ->
            Text(
                text = symptom.name,
                modifier = Modifier
                    .heightIn(min = 20.dp)
                    .shadow(elevation = 3.dp, shape = CircleShape)
                    .background(color = white, shape = CircleShape)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                style = MaterialTheme.typography.textField.copy(
                    color = darkPurple,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@Preview
@Composable
fun WellbeingCellPreview() {
    WellbeingCell(modifier = Modifier, feeling = Feelings.Sad)
}

@ExperimentalMaterial3Api
@Composable
private fun HomeButton(
    item: HomeItems.ButtonHome,
    modifier: Modifier,
    onItemClick: (HomeItems.ButtonHome) -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        colors = CardDefaults.cardColors(white),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                onItemClick(item)
            }) {
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .alpha(0.2f),
                painter = painterResource(id = R.drawable.mask_group),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                )
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.body1.copy(color = gray),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                )
                item.arrow?.let { arrowId ->
                    Image(
                        painter = rememberAsyncImagePainter(model = arrowId),
                        contentDescription = "",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically),
                    )
                }
            }
        }
    }
}
