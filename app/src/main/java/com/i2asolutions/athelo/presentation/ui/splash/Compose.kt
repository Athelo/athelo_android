@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.CircleProgress
import com.i2asolutions.athelo.presentation.ui.theme.button
import com.i2asolutions.athelo.presentation.ui.theme.lightPurple

@Composable
fun SplashScreen(viewModel: SplashViewModel) {
    viewModel.handleEvent(SplashEvent.InitApp)
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
                .align(Alignment.Center)
                .padding(56.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "Athelo"
            )
            Spacer(
                modifier = Modifier
                    .height(26.dp)
            )
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
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
        }
    }
}