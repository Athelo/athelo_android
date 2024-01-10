package com.athelohealth.mobile.presentation.ui.share.appInfo

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
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.widgets.HtmlText

@Composable
fun AppInfoScreen(viewModel: AppInfoViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { viewState.value.isLoading }) {
        Column {
            ToolbarWithNameBack(
                screenName = stringResource(id = viewState.value.screenName),
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
                text = viewState.value.text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = gray,
                    textAlign = TextAlign.Justify
                ),
                linkClicked = { viewModel.handleEvent(AppInfoEvent.LinkClicked(it)) }
            )
        }
    }
}
