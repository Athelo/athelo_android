package com.i2asolutions.athelo.presentation.ui.share.appInfo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithNameBack
import com.i2asolutions.athelo.presentation.ui.theme.gray
import com.i2asolutions.athelo.widgets.HtmlText

@Composable
fun AppInfoScreen(viewModel: AppInfoViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.value.isLoading }) {
        Column {
            ToolbarWithNameBack(
                screenName = stringResource(id = state.value.screenName),
                backClick = {
                    viewModel.handleEvent(AppInfoEvent.BackClicked)
                }
            )

            Image(
                modifier = Modifier.align(CenterHorizontally),
                painter = painterResource(id = R.drawable.athelo_logo_horizontal),
                contentDescription = stringResource(id = R.string.app_name)
            )

            HtmlText(
                modifier = Modifier
                    .navigationBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .weight(1f),
                text = state.value.text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = gray,
                    textAlign = TextAlign.Justify
                ),
                linkClicked = { viewModel.handleEvent(AppInfoEvent.LinkClicked(it)) }
            )
        }
    }
}
