@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
package com.i2asolutions.athelo.presentation.ui.share.news

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.news.News
import com.i2asolutions.athelo.presentation.model.news.NewsData
import com.i2asolutions.athelo.presentation.model.news.NewsListType
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.contentful.ContentfulClient

@Composable
fun ContentfulNewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        modifier = Modifier.statusBarsPadding(),
        viewModel = viewModel, showProgressProvider = { state.value.isLoading },
        content = Content(state = state, viewModel = viewModel, handleEvent = viewModel::handleEvent)
    )
}

@Composable
private fun Content(
    state: State<NewsViewState>,
    viewModel: NewsViewModel,
    handleEvent: (NewsEvent) -> Unit = {}
): @Composable (BoxScope.() -> Unit) =
    {
        val contentfulViewState = viewModel.contentfulViewState.collectAsState()
        Column {
            ToolbarWithMenuAndMyProfile(
                userAvatar = state.value.currentUser.photo?.image5050,
                userDisplayName = state.value.currentUser.displayName ?: "",
                menuClick = {
                    handleEvent(NewsEvent.MenuClick)
                },
                avatarClick = {
                    handleEvent(NewsEvent.MyProfileClick)
                })
            val focusManager = LocalFocusManager.current
            RadioButtonGroup(
                selectionProvider = {
                    if (state.value.screenType == NewsListType.Favourites) 1 else 0
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                buttons = arrayOf(
                    RadioButton(
                        text = stringResource(id = R.string.All_News),
                        onClick = {
                            handleEvent(NewsEvent.ListButtonClick)
                        }
                    ),
                    RadioButton(
                        text = stringResource(id = R.string.Favorites),
                        onClick = {
                            handleEvent(NewsEvent.FavoriteButtonClick)
                        })
                )
            )
            val lazyState = rememberLazyListState()
            LaunchedEffect(key1 = null) {
                lazyState.scrollToItem(0)
            }
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = false),
                onRefresh = { viewModel.loadData() }) {
                LazyColumn(
                    modifier = Modifier,
                    state = lazyState,
                    contentPadding = PaddingValues(bottom = 56.dp)
                ) {
                    stickyHeader {
                        Box {
                            Text(
                                text = "", modifier = Modifier
                                    .height(54.dp)
                                    .fillMaxWidth()
                                    .background(background)
                            )
                            SearchInputTextField(
                                initialValue= viewModel.currentQuery(),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 24.dp, top = 24.dp),
                                onChange = { search ->
                                    viewModel.updateNewsList(search.text)
                                },
                                keyboardActions = KeyboardActions(onSearch = {
                                    focusManager.clearFocus()
                                }),
                                hint = stringResource(id = R.string.Search)
                            )
                        }
                    }
                    if (contentfulViewState.value.isEmpty()) {
                        item {
                            NoItemsScreen()
                        }
                    } else {
                        items(contentfulViewState.value, key = { it.key }) { news ->
                            ImageNewsCell(
                                news = news,
                                modifier = Modifier.padding(vertical = 12.dp),
                                handleEvent = { viewModel.handleEvent(it)}
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
private fun NoItemsScreen() {
    Image(
        painter = painterResource(id = R.drawable.no_news_content),
        contentDescription = null,
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth()
            .aspectRatio(272 / 252f)
    )
    Text(
        text = stringResource(id = R.string.No_content_available),
        style = MaterialTheme.typography.headline20.copy(
            color = black,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 32.dp, bottom = 16.dp)
            .fillMaxWidth()
    )
    Text(
        text = stringResource(id = R.string.It_looks_like_this_page_is_currently_empty),
        style = MaterialTheme.typography.paragraph.copy(
            color = gray,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun ImageNewsCell(news: NewsData, modifier: Modifier, handleEvent: (NewsEvent) -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(135.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable { handleEvent(NewsEvent.NewsDetails(newsId = news.key)) },
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(30.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ImageWithProgress(
                image = news.image,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(112 / 135f),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = ContentfulClient.getText(context = LocalContext.current, data = news.body),
                    style = MaterialTheme.typography.subtitle.copy(fontWeight = FontWeight.Normal),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NoImageNewsCell(news: News, modifier: Modifier, handleEvent: (NewsEvent) -> Unit) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable { handleEvent(NewsEvent.NewsItemClick(news)) },
        colors = CardDefaults.cardColors(containerColor = white),
        shape = RoundedCornerShape(30.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                news.categories.forEach { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(lightGreen.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
            Text(
                text = news.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = news.description,
                style = MaterialTheme.typography.subtitle.copy(fontWeight = FontWeight.Normal),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
private fun NoImageNewsCellPreview() {
    NoImageNewsCell(
        news = News(
            id = 1,
            isFavourite = false,
            name = "Test name with maybe two lines of text?",
            description = LoremIpsum(29).values.joinToString(" "),
            categories = listOf("Cat 1", "Cat 2"),
            image = com.i2asolutions.athelo.presentation.model.base.Image(image250250 = "https://placekitten.com/250/250")
        ), modifier = Modifier, handleEvent = {}
    )
}