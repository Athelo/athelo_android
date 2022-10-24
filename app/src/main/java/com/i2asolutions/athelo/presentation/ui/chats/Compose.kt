@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.i2asolutions.athelo.presentation.ui.chats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.createMockConversations

@Composable
fun ChatListScreen(viewModel: ChatListViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        includeStatusBarPadding = false
    ) {
        if (state.value.showLanding) {
            ChatGroupLanding(
                handleEvent = viewModel::handleEvent,
                userDisplayName = state.value.currentUser.displayName ?: "",
                userAvatar = state.value.currentUser.photo?.image5050
            )
        } else {
            ChatConversationsScreen(
                handleEvent = viewModel::handleEvent,
                yourConversation = state.value.yourConversation,
                otherConversation = state.value.conversations,
                userAvatar = state.value.currentUser.photo?.image5050,
                userDisplayName = state.value.currentUser.displayName ?: "",
                showLoadMore = { state.value.canLoadNextPage }
            )
        }
    }
}

@Composable
fun ChatGroupLanding(
    handleEvent: (ChatListEvent) -> Unit,
    userAvatar: String?,
    userDisplayName: String
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
    ) {
        Box {
            Image(
                painter = painterResource(id = R.drawable.chat_group_lets_get_started),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            ToolbarWithMenuAndMyProfile(
                userAvatar = userAvatar,
                userDisplayName = userDisplayName,
                menuClick = {
                    handleEvent(ChatListEvent.MenuClick)
                },
                avatarClick = {
                    handleEvent(ChatListEvent.MyProfileClick)
                })
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.chat_group_landing_header),
            style = MaterialTheme.typography.paragraph.copy(
                color = darkPurple,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            text = stringResource(id = R.string.chat_group_landing_message),
            style = MaterialTheme.typography.body1.copy(
                color = gray,
                textAlign = TextAlign.Center
            )
        )
        MainButton(
            textId = R.string.Let_s_Start,
            background = purple,
            onClick = {
                handleEvent(ChatListEvent.LetStartClick)
            },
        )
    }
}

@Composable
fun ChatConversationsScreen(
    handleEvent: (ChatListEvent) -> Unit,
    yourConversation: List<Conversation>,
    otherConversation: List<Conversation>,
    showLoadMore: () -> Boolean,
    userAvatar: String?,
    userDisplayName: String
) {
    Column(
        modifier = Modifier
    ) {
        ToolbarWithMenuAndMyProfile(
            userAvatar = userAvatar,
            userDisplayName = userDisplayName,
            menuClick = {
                handleEvent(ChatListEvent.MenuClick)
            },
            avatarClick = {
                handleEvent(ChatListEvent.MyProfileClick)
            })
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = false),
            onRefresh = { handleEvent(ChatListEvent.RefreshData) }) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 56.dp)
            ) {
                item {
                    Text(
                        text = stringResource(id = R.string.Hello_we_are_glad_you_are_here),
                        style = MaterialTheme.typography.headline20.copy(
                            color = darkPurple,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 8.dp, top = 24.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.These_are_the_communities_we_found_after_your_request),
                        style = MaterialTheme.typography.body1.copy(
                            color = gray,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                    )
                }
                if (yourConversation.isNotEmpty()) {
                    headerItem(if (yourConversation.size == 1) R.string.Your_Community else R.string.Your_Communities)
                    items(yourConversation, key = { it.conversationId.toString() + "y" }) {
                        CommunityCell(conversation = it, handleEvent = handleEvent)
                    }
                    if (otherConversation.isNotEmpty())
                        headerItem(R.string.Other_communities)
                } else {
                    headerItem(R.string.Communities)
                }
                items(otherConversation, key = { it.conversationId }) {
                    CommunityCell(conversation = it, handleEvent = handleEvent)
                }
                if (showLoadMore()) {
                    item {
                        Pagination(handleEvent = handleEvent)
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CommunityCell(conversation: Conversation, handleEvent: (ChatListEvent) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .shadow(shape = RoundedCornerShape(30.dp), elevation = 5.dp)
            .clickable {
                handleEvent(ChatListEvent.ConversationClick(conversation.conversationId))
            },
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = white
        )
    ) {
        Text(
            text = conversation.name,
            style = MaterialTheme.typography.headline20.copy(
                color = gray,
                textAlign = TextAlign.Left
            ),
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
        )
        if (conversation.users.isNotEmpty())
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                conversation.users.forEachIndexed { index, user ->
                    CircleAvatarImage(
                        avatar = user.photo?.image5050,
                        displayName = user.displayName,
                        borderColor = white,
                        includeBorder = true,
                        borderWidth = 2.dp,
                        modifier = Modifier
                            .zIndex((conversation.users.size - index).toFloat())
                            .padding(start = (index * 22).dp)
                            .shadow(0.dp)
                    )
                }
            }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (conversation.participantsCount <= 0) stringResource(id = R.string.No_participants) else pluralStringResource(
                    id = R.plurals.x_participants,
                    conversation.participantsCount,
                    conversation.participantsCount,
                ),
                style = MaterialTheme.typography.body1.copy(gray)
            )
            Spacer(modifier = Modifier.weight(1f))
            if (conversation.myConversation) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_comment_check),
                    contentDescription = null,
                    tint = lightOlivaceous,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp)
                )
                Text(
                    text = stringResource(id = R.string.You_are_member),
                    style = MaterialTheme.typography.body1.copy(
                        color = lightOlivaceous,
                        textAlign = TextAlign.Center
                    )
                )
            } else {
                Button(
                    onClick = { handleEvent(ChatListEvent.ConversationClick(conversation.conversationId)) },
                    modifier = Modifier
                        .wrapContentHeight()
                        .shadow(
                            0.dp,
                            shape = RoundedCornerShape(13.dp),
                            ambientColor = lightPurple
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lightPurple.copy(alpha = 0.1f),
                    ),
                    shape = RoundedCornerShape(13.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.Open_community),
                        style = MaterialTheme.typography.body1.copy(color = darkPurple)
                    )
                }
            }
        }
    }
}

private fun LazyListScope.headerItem(titleId: Int) {
    item {
        Text(
            text = stringResource(titleId),
            style = MaterialTheme.typography.headline20.copy(
                color = gray,
                textAlign = TextAlign.Left
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Pagination(handleEvent: (ChatListEvent) -> Unit) {
    Box(
        Modifier
            .requiredHeight(80.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(key1 = true) {
            handleEvent(ChatListEvent.LoadNextPage)
        }
        CircleProgress(
            modifier = Modifier
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun LandingPreview() {
    ChatGroupLanding(handleEvent = {}, userAvatar = null, userDisplayName = "Jan Test")
}

@Preview
@Composable
fun ListPreview() {
    Box(modifier = Modifier.background(background)) {
        ChatConversationsScreen(
            handleEvent = {},
            yourConversation = createMockConversations(5, true),
            otherConversation = createMockConversations(5, false),
            userAvatar = null,
            userDisplayName = "Jan Test",
            showLoadMore = { true }
        )
    }
}