package com.i2asolutions.athelo.presentation.ui.caregiver.lostCaregiver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LostCaregiverScreen(viewModel: LostCaregiverViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel, showProgressProvider = { state.isLoading },
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = black.copy(alpha = 0.3f),
        includeStatusBarPadding = false,
    ) {
        Content(handleEvent = viewModel::handleEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BoxScope.Content(handleEvent: (LostCaregiverEvent) -> Unit) {
    val configuration = LocalConfiguration.current
    val coroutineScope = rememberCoroutineScope()
    val show = rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        coroutineScope.launch {
            delay(300)
            show.value = true
        }
    }
    AnimatedVisibility(
        visible = show.value,
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
            shape = RoundedCornerShape(30.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 40.dp)
                    .height(42.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(id = R.string.Sorry),
                style = MaterialTheme.typography.headline20.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.Lost_Caregiver_message),
                style = MaterialTheme.typography.paragraph.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 40.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                MainButton(
                    textId = R.string.OK,
                    modifier = Modifier.width(140.dp),
                    onClick = {
                        handleEvent(LostCaregiverEvent.OkButtonClick)
                    })
            }
        }
    }
}

@Preview
@Composable
fun DeletePopupPrev() {
    AtheloTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(black.copy(alpha = 0.3f))) {
            Content(handleEvent = {})
        }
    }
}