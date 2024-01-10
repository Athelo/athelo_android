package com.athelohealth.mobile.presentation.ui.share.newsDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.ImageWithProgress
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBackFavourite
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun NewsDetailScreen(viewModel: NewsDetailViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { viewState.value.isLoading }) {
        Column(modifier = Modifier.composed {
            if (viewState.value.news.articleLink.isNullOrBlank())
                Modifier
            else Modifier
                .padding(bottom = 50.dp)
                .navigationBarsPadding()
        }) {
            ToolbarWithNameBackFavourite(
                favouriteClick = { viewModel.handleEvent(NewsDetailEvent.FavoriteButtonClick) },
                backClick = { viewModel.handleEvent(NewsDetailEvent.BackButtonClick) },
                screenName = stringResource(id = R.string.Article),
                favourite = { viewState.value.news.isFavourite }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                val image = viewState.value.news.image?.image
                if (!image.isNullOrBlank()) {
                    ImageWithProgress(
                        image,
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(375 / 237f),
                        contentScale = ContentScale.Crop
                    )
                }
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {
                    Text(
                        text = viewState.value.news.name,
                        style = MaterialTheme.typography.headline20.copy(
                            color = black,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .alignByBaseline()
                    )
                    Text(
                        text = viewState.value.news.createDate,
                        style = MaterialTheme.typography.body1.copy(
                            color = darkPurple,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Text(
                    text = viewState.value.news.description,
                    style = MaterialTheme.typography.paragraph.copy(textAlign = TextAlign.Justify),
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(76.dp))
            }
        }
        if (!viewState.value.news.articleLink.isNullOrBlank())
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
