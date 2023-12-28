package com.athelohealth.mobile.presentation.ui.share.messages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.displayAsString
import com.athelohealth.mobile.presentation.model.chat.PrivateConversation
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.CircleAvatarImage
import com.athelohealth.mobile.presentation.ui.base.ToolbarWithNameBack
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
fun MessageScreen(viewModel: MessageViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Content(handleEvent = viewModel::handleEvent, messages = state.messages)
    }
}

@Composable
private fun Content(handleEvent: (MessageEvent) -> Unit, messages: List<PrivateConversation>) {
    Column {
        ToolbarWithNameBack(
            backClick = { handleEvent(MessageEvent.BackButtonClick) },
            screenName = stringResource(id = R.string.Messages)
        )
        LazyColumn(
            contentPadding = PaddingValues(),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(items = messages, key = { message -> message.conversationId }) {
                MessageCell(handleEvent = handleEvent, message = it)
            }
        }
    }
}

@Composable
private fun MessageCell(handleEvent: (MessageEvent) -> Unit, message: PrivateConversation) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.clickable {
        handleEvent(MessageEvent.ConversationItemClick(message))
    }) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleAvatarImage(
                modifier = Modifier.size(50.dp),
                avatar = message.user.photo?.image100100,
                displayName = message.user.displayName
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(0.75f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = message.user.displayName,
                    style = MaterialTheme.typography.textField.copy(
                        color = black,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = message.lastMessage,
                    style = MaterialTheme.typography.body1.copy(
                        color = gray,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier.weight(0.25f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = message.lastMessageDate?.displayAsString("dd/MM") ?: "",
                    style = MaterialTheme.typography.subtitle.copy(
                        color = lightGray,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (message.unreadMessages > 0) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .widthIn(min = 24.dp)
                            .background(red, CircleShape)
                            .padding(horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = message.unreadMessages.toString(),
                            style = MaterialTheme.typography.subtitle.copy(
                                color = white,
                                textAlign = TextAlign.Center
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier,
                            maxLines = 1,
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
        Divider(thickness = 1.dp, color = lightGray)
    }
}