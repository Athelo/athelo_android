@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.CircleProgress
import com.athelohealth.mobile.presentation.ui.base.PinEditText
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        modifier = Modifier.navigationBarsPadding(),
        showProgressProvider = { false }) {
        Image(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.frame_1640),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 56.dp, end = 56.dp),
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "Athelo"
            )
            Spacer(modifier = Modifier.height(26.dp))
            if (viewState.showPin) {
                CodeInput(handle = viewModel::handleEvent, modifier = Modifier.imePadding())
            } else {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 56.dp),
                    text = stringResource(id = R.string.Loading),
                    style = MaterialTheme.typography.button,
                    color = lightPurple
                )
                Spacer(modifier = Modifier.height(9.dp))
                CircleProgress(
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .align(Alignment.CenterHorizontally)
                )
                LaunchedEffect(key1 = true) {
                    viewModel.handleEvent(SplashEvent.InitApp)
                }
            }
        }
    }
}

@Composable
fun CodeInput(handle: (SplashEvent) -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box(modifier = modifier.height(132.dp)) {
            Image(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 80.dp)
                    .alpha(0.2f)
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.frame_3741),
                contentScale = ContentScale.FillWidth,
                contentDescription = ""
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(100.dp)
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.Pin_message),
                    style = MaterialTheme.typography.paragraph.copy(
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    ),
                    color = black
                )
                PinEditText(enteredPin = { handle(SplashEvent.EnterPin(it)) })
            }
        }
    }
}