@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.i2asolutions.athelo.presentation.ui.symptomInfo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun InfoSymptomScreen(viewModel: InfoSymptomViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = black.copy(alpha = 0.3f),
        includeStatusBarPadding = false,
        showProgressProvider = {
            false
        },
        viewModel = viewModel
    ) {
        val configuration = LocalConfiguration.current
        val coroutineScope = rememberCoroutineScope()
        var show by rememberSaveable {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true) {
            coroutineScope.launch {
                delay(300)
                show = true
            }
        }
        AnimatedVisibility(
            visible = show,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { configuration.screenHeightDp },
                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            ),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Card(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = white),
                shape = RoundedCornerShape(30.dp),
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.athelo_logo_with_text),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.Center)
                            .height(54.dp)
                            .width(113.dp)
                    )
                    IconButton(
                        onClick = { viewModel.handleEvent(InfoSymptomEvent.BackButtonClick) },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fi_sr_cross_small),
                            contentDescription = "Close Button",
                            tint = gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Text(
                    text = state.title,
                    style = MaterialTheme.typography.headline20.copy(color = darkPurple),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
                if(state.comment.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(
                            top = 24.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        textAlign = TextAlign.Start,
                        text = stringResource(id = R.string.Comment),
                        color = gray,
                        style = MaterialTheme.typography.textField.copy(
                            fontSize = 16.sp,
                            fontFamily = fonts
                        ),
                    )
                    Text(
                        text = state.comment,
                        style = MaterialTheme.typography.textField.copy(color = darkPurple),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }
                MainButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                        .fillMaxWidth(),
                    textId = R.string.Symptoms_Recommendation,
                    onClick = {
                        viewModel.handleEvent(InfoSymptomEvent.RecommendationButtonClick)
                    })
            }
        }

    }
}