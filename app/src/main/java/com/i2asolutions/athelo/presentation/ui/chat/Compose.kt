@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.displayAsChatDate
import com.i2asolutions.athelo.extensions.displayAsChatSeparator
import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.presentation.model.chat.ConversationInfo
import com.i2asolutions.athelo.presentation.model.member.User
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.createMockConversation
import com.i2asolutions.athelo.utils.createMockMessages
import com.i2asolutions.athelo.utils.createMockUser
import java.util.*

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        includeStatusBarPadding = false
    ) {
        ChatConversationScreen(
            handleEvent = viewModel::handleEvent,
            conversation = state.value.conversation,
            messages = state.value.messages,
            yourConversation = state.value.conversation?.myConversation == true,
            showLoadMore = { state.value.canLoadNextPage },
            currentUser = state.value.currentUser,
            showMoreMenu = state.value.showMorePopup,
//            isMuted = state.value.isMuted,
            shouldScrollToBottom = state.value.shouldScrollToBottom,
            showHelloMessage = state.value.shouldShowHello
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatConversationScreen(
    handleEvent: (ChatEvent) -> Unit,
    conversation: Conversation?,
    messages: List<ConversationInfo.ConversationMessage>,
    showLoadMore: () -> Boolean,
    yourConversation: Boolean,
    currentUser: User,
    showMoreMenu: Boolean,
//    isMuted: Boolean,
    shouldScrollToBottom: Boolean,
    showHelloMessage: Boolean
) {
    val listState = rememberLazyListState()
    LaunchedEffect(key1 = messages.size) {
        if (shouldScrollToBottom) listState.animateScrollToItem(0)
    }
    LaunchedEffect(key1 = listState.isScrollInProgress) {
        handleEvent(ChatEvent.LastVisibleElement(listState.firstVisibleItemIndex))
    }

    Column {
        ToolbarForChat(
            conversation = conversation,
            backClick = {
                handleEvent(ChatEvent.BackClick)
            },
            moreClick = {
                handleEvent(ChatEvent.MoreClick(it))
            },
            showMoreButton = conversation?.myConversation == true,
            showPopup = showMoreMenu,
//            isMuted = isMuted
        )

        Box {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(id = R.drawable.chat_bg),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier
                    .blur(16.dp)
                    .fillMaxSize()
            )
            Column(modifier = Modifier.navigationBarsPadding()) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 30.dp),
                    reverseLayout = true,
                    verticalArrangement = Arrangement.Bottom,
                    state = listState,
                ) {
                    if (showHelloMessage)
                        item {
                            HelloMessage(handleEvent = handleEvent)
                        }
                    itemsIndexed(
                        messages,
                        key = { _, item -> item.messageId },
                        contentType = { _, message -> getContentType(currentUser, message) }
                    ) { index, _ ->
                        ChatMessage(messages, index, currentUser, showLoadMore())
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
                if (yourConversation) {
                    ChatInput(sendButtonClick = { handleEvent(ChatEvent.SendMessageClicked(it)) })
                } else {
                    SecondaryButton(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        text = R.string.Join_The_Community,
                        background = white,
                        border = purple,
                        onClick = {
                            handleEvent(ChatEvent.JoinConversationClicked)
                        },
                    )
                }
            }
        }
    }

//    System.err.println("aaa scroll to last item")
//    if (isScrolledToBottom) {
//        LaunchedEffect(key1 = true) {
//            isScrolledToBottom = false
////            scope.launch {
//            System.err.println("scroll to last item")
//            listState.animateScrollToItem(0)
////            }
//        }
//    }
}

@Composable
fun ChatMessage(
    messages: List<ConversationInfo.ConversationMessage>,
    index: Int,
    currentUser: User,
    canLoadMore: Boolean
) {
    val message = messages[index]
    val prevMessage = if (index >= messages.size - 1) null else messages[index + 1]
    val nextMessage = if (index == 0) null else messages[index - 1]


    val shouldAddSeparator = shouldAddDaySeparator(nextMessage, message, canLoadMore)
    if (shouldAddSeparator)
        SystemMessage(message = nextMessage?.date.displayAsChatSeparator())

    val isSeparatorAfterMessage = shouldAddDaySeparator(message, prevMessage, canLoadMore)
    when {
        message.user == null -> {
            SystemMessage(message = message)
        }
        currentUser.userId != null && message.userId.startsWith(currentUser.userId) -> MyMessage(
            message = message,
            prevMessage = prevMessage,
            nextMessage = nextMessage,
            isSeparatorBeforeMessage = shouldAddSeparator,
            isSeparatorAfterMessage = isSeparatorAfterMessage
        )
        else -> OtherUserMessage(
            message = message,
            prevMessage = prevMessage,
            nextMessage = nextMessage,
            isSeparatorBeforeMessage = shouldAddSeparator,
            isSeparatorAfterMessage = isSeparatorAfterMessage
        )
    }
}

@Composable
fun OtherUserMessage(
    message: ConversationInfo.ConversationMessage,
    prevMessage: ConversationInfo.ConversationMessage?,
    nextMessage: ConversationInfo.ConversationMessage?,
    isSeparatorBeforeMessage: Boolean,
    isSeparatorAfterMessage: Boolean
) {
    val nameVisible = isSeparatorAfterMessage || prevMessage?.userId != message.userId
    val avatarVisible = isSeparatorBeforeMessage || nextMessage?.userId != message.userId
    val timeVisible = nextMessage?.userId != message.userId
    val bottomMargin = if (!isSeparatorBeforeMessage && nextMessage?.userId == message.userId)
        8.dp
    else
        24.dp

    Column(
        modifier = Modifier
            .padding(bottom = bottomMargin)
            .padding(horizontal = 16.dp)
    ) {
        Row {
            if (avatarVisible)
                CircleAvatarImage(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Top),
                    avatar = message.user?.photo?.get100orBigger(),
                    displayName = message.user?.displayName ?: ""
                )
            else
                Box(modifier = Modifier.size(42.dp))
            Card(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = white
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp, top = 4.dp)
                ) {
                    if (nameVisible)
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = message.user?.displayName ?: "",
                            style = MaterialTheme.typography.subtitle.copy(color = darkPurple)
                        )
                    else
                        Box(modifier = Modifier.height(12.dp))
                    Text(
                        text = message.message,
                        style = MaterialTheme.typography.textField.copy(
                            color = black,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                    )
                }
            }
        }
        if (timeVisible)
            Text(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(End),
                text = message.date.displayAsChatDate(),
                style = MaterialTheme.typography.subtitle.copy(color = white)
            )
    }
}

@Composable
fun MyMessage(
    message: ConversationInfo.ConversationMessage,
    prevMessage: ConversationInfo.ConversationMessage?,
    nextMessage: ConversationInfo.ConversationMessage?,
    isSeparatorBeforeMessage: Boolean,
    isSeparatorAfterMessage: Boolean
) {
    val nameVisible = isSeparatorAfterMessage || prevMessage?.userId != message.userId
    val timeVisible = nextMessage?.userId != message.userId
    val bottomMargin = if (!isSeparatorBeforeMessage && nextMessage?.userId == message.userId)
        8.dp
    else
        24.dp
    Column(
        modifier = Modifier
            .padding(bottom = bottomMargin)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .align(End)
                .padding(start = 8.dp)
                .shadow(shape = RoundedCornerShape(12.dp), elevation = 5.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = lightPurple
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp, top = 4.dp)
            ) {
                if (nameVisible)
                    Text(
                        modifier = Modifier.padding(bottom = 4.dp),
                        text = stringResource(id = R.string.Me),
                        style = MaterialTheme.typography.subtitle.copy(color = white)
                    )
                else
                    Box(modifier = Modifier.height(12.dp))
                Text(
                    text = message.message,
                    style = MaterialTheme.typography.textField.copy(
                        color = white,
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )
                )
            }
        }
        if (timeVisible)
            Text(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .align(End),
                text = message.date.displayAsChatDate(),
                style = MaterialTheme.typography.subtitle.copy(color = white)
            )
    }
}

@Composable
fun SystemMessage(message: ConversationInfo.ConversationMessage) {
    SystemMessage(message = message.message)
}

@Composable
fun SystemMessage(message: String) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = message,
            style = MaterialTheme.typography.subtitle.copy(
                color = white,
                fontStyle = FontStyle.Italic
            ),
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                .background(darkPurple.copy(0.48f), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun HelloMessage(handleEvent: (ChatEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Center)
                .padding(24.dp)
                .background(lightPurple, RoundedCornerShape(17.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { handleEvent(ChatEvent.SayHelloToEveryone) }
        ) {
            Image(
                modifier = Modifier.align(CenterVertically),
                painter = painterResource(id = R.drawable.ic_hello_white),
                contentDescription = stringResource(
                    id = R.string.app_name
                )
            )
            Text(
                text = stringResource(id = R.string.Hello_to_everyone_),
                style = MaterialTheme.typography.subtitle.copy(color = white),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

fun shouldAddDaySeparator(
    nextMessage: ConversationInfo.ConversationMessage?,
    message: ConversationInfo.ConversationMessage?,
    canLoadMore: Boolean
): Boolean {
    if (nextMessage == null || message == null) {
        return !canLoadMore
    }
    val calendar = Calendar.getInstance()
    calendar.time = nextMessage.date
    val prevYear = calendar.get(Calendar.YEAR)
    val prevDay = calendar.get(Calendar.DAY_OF_YEAR)
    calendar.time = message.date
    val thisYear = calendar.get(Calendar.YEAR)
    val thisDay = calendar.get(Calendar.DAY_OF_YEAR)
    return thisYear != prevYear || thisDay != prevDay
}

@Composable
private fun Pagination(handleEvent: (ChatEvent) -> Unit) {
    Box(
        Modifier
            .requiredHeight(80.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(key1 = true) {
            handleEvent(ChatEvent.LoadNextPage)
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
fun ListPreview() {
    Box(modifier = Modifier.background(background)) {
        ChatConversationScreen(
            handleEvent = {},
            conversation = createMockConversation(7, true),
            messages = createMockMessages(20),
            yourConversation = true,
            showLoadMore = { true },
            currentUser = createMockUser(2),
            showMoreMenu = true,
//            isMuted = false,
            shouldScrollToBottom = false,
            showHelloMessage = true
        )
    }
}

private fun getContentType(currentUser: User, item: ConversationInfo.ConversationMessage): Any {
    return if (item.user == currentUser) ChatMessageType.MyMessage
    else if (item.user != null) ChatMessageType.OtherMessage
    else ChatMessageType.SystemMessage
}

enum class ChatMessageType {
    MyMessage, OtherMessage, SystemMessage
}