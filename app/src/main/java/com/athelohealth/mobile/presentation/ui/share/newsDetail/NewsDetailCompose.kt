package com.athelohealth.mobile.presentation.ui.share.newsDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ShowContentfulNews
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBackFavourite

@Composable
fun ContentfulNewsDetailScreen(viewModel: NewsDetailViewModel) {
    val state = viewModel.state.collectAsState()
    val contentfulViewState = viewModel.contentfulViewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.value.isLoading }) {
        Column(modifier = Modifier.composed {
            if (state.value.news.articleLink.isNullOrBlank())
                Modifier
            else Modifier
                .padding(bottom = 50.dp)
                .navigationBarsPadding()
        }) {
            ToolbarWithNameBackFavourite(
                favouriteClick = {  },
                backClick = { viewModel.handleEvent(NewsDetailEvent.BackButtonClick) },
                screenName = stringResource(id = R.string.Article),
                favourite = { false }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                ShowContentfulNews(contentfulViewState.value)
            }
        }
        if (!state.value.news.articleLink.isNullOrBlank())
            MainButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .fillMaxWidth(),
                textId = R.string.Read_Full_Article,
                onClick = {
                    viewModel.handleEvent(NewsDetailEvent.LinkButtonClick)
                })
    }
}
