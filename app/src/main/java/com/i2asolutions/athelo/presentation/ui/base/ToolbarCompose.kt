package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.chat.ChatMoreButtonClickAction
import com.i2asolutions.athelo.presentation.model.chat.Conversation
import com.i2asolutions.athelo.presentation.model.chat.SimpleUser
import com.i2asolutions.athelo.presentation.ui.theme.body1
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.gray
import com.i2asolutions.athelo.presentation.ui.theme.headline20

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    screenName: String = "",
    showBack: Boolean = false,
    onBackClick: () -> Unit = {},
    backIcon: Int = R.drawable.ic_back_arrow
) {
    Row(modifier = modifier.height(56.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        if (showBack)
            Image(
                painter = painterResource(id = backIcon),
                contentDescription = "Back Button",
                modifier = Modifier
                    .size(56.dp)
                    .clickable(onClick = onBackClick),
                contentScale = ContentScale.Inside
            )
        else Spacer(Modifier.width(56.dp))

        val textSize = remember { mutableStateOf(20.sp) }
        Text(
            text = screenName,
            style = MaterialTheme.typography.headline20.copy(color = gray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterVertically)
                .padding(end = 56.dp),
            maxLines = 1,
            fontSize = textSize.value,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    textSize.value = textSize.value.times(0.9f)
                }
            },
        )
    }
}

@Composable
fun ImageToolbar(
    modifier: Modifier = Modifier,
    leftButton: (@Composable RowScope.() -> Unit)? = null,
    rightButton: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(modifier = modifier.height(56.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        leftButton?.invoke(this) ?: Spacer(modifier = Modifier.height(56.dp))
        Image(
            painter = painterResource(id = R.drawable.athelo_logo_with_text),
            contentDescription = "Logo",
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .padding(vertical = 8.dp),
            contentScale = ContentScale.FillHeight
        )
        rightButton?.invoke(this) ?: Spacer(modifier = Modifier.height(56.dp))
    }
}

@Composable
fun ScreenNameToolbar(
    modifier: Modifier = Modifier,
    screenName: String,
    leftButton: (@Composable RowScope.() -> Unit)? = null,
    rightButton: (@Composable RowScope.() -> Unit)? = null,
) {
    Row(modifier = modifier.height(56.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        leftButton?.invoke(this) ?: Spacer(modifier = Modifier.height(56.dp))
        val textSize = remember { mutableStateOf(20.sp) }
        Text(
            text = screenName,
            style = MaterialTheme.typography.headline20.copy(color = gray),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .align(CenterVertically),
            maxLines = 1,
            fontSize = textSize.value,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    textSize.value = textSize.value.times(0.9f)
                }
            },
        )
        rightButton?.invoke(this) ?: Spacer(modifier = Modifier.height(56.dp))
    }
}

@Composable
fun ToolbarWithNameBackFavourite(
    modifier: Modifier = Modifier,
    favouriteClick: () -> Unit,
    backClick: () -> Unit,
    screenName: String,
    favourite: () -> Boolean,
) {
    ScreenNameToolbar(
        modifier = modifier,
        screenName = screenName,
        leftButton = {
            BackButton(modifier = Modifier) {
                backClick()
            }
        },
        rightButton = {
            FavouriteButton(modifier = Modifier, initFavouriteState = favourite) {
                favouriteClick()
            }
        })
}

@Composable
fun ToolbarWithMenuAndMyProfile(
    modifier: Modifier = Modifier,
    userAvatar: String?,
    userDisplayName: String,
    menuClick: () -> Unit,
    avatarClick: () -> Unit,
) {
    ImageToolbar(
        modifier
            .statusBarsPadding()
            .padding(end = 16.dp)
            .padding(bottom = 24.dp),
        leftButton = {
            MenuButton {
                menuClick()
            }
        },
        rightButton = {
            val avatar = remember(userAvatar) {
                userAvatar
            }
            CircleAvatarImage(
                avatar = avatar,
                displayName = userDisplayName,
                modifier = Modifier
                    .size(32.dp)
                    .align(CenterVertically)
                    .clip(CircleShape)
                    .clickable {
                        avatarClick()
                    }
            )
        })
}

@Composable
fun ToolbarWithBackAndMyProfile(
    modifier: Modifier = Modifier,
    userAvatar: String?,
    userDisplayName: String,
    backClick: () -> Unit,
    avatarClick: () -> Unit,
) {
    ImageToolbar(
        modifier
            .statusBarsPadding()
            .padding(end = 16.dp)
            .padding(bottom = 24.dp),
        leftButton = {
            BackButton(modifier = Modifier) {
                backClick()
            }
        },
        rightButton = {
            CircleAvatarImage(
                avatar = userAvatar,
                displayName = userDisplayName,
                modifier = Modifier
                    .size(32.dp)
                    .align(CenterVertically)
                    .clip(CircleShape)
                    .clickable {
                        avatarClick()
                    }
            )
        })
}


@Composable
fun ToolbarWithMyProfile(
    modifier: Modifier = Modifier,
    avatarClick: () -> Unit,
    userAvatar: String?,
    userDisplayName: String,
) {
    ImageToolbar(
        modifier
            .statusBarsPadding()
            .padding(end = 16.dp)
            .padding(bottom = 24.dp),
        leftButton = {
            Spacer(modifier = Modifier.size(56.dp))
        },
        rightButton = {
            CircleAvatarImage(
                avatar = userAvatar,
                displayName = userDisplayName,
                modifier = Modifier
                    .size(32.dp)
                    .align(CenterVertically)
                    .clip(CircleShape)
                    .clickable {
                        avatarClick()
                    }
            )
        })
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ToolbarForChat(
    modifier: Modifier = Modifier,
    moreClick: (ChatMoreButtonClickAction) -> Unit = {},
    backClick: () -> Unit = {},
    showMoreButton: Boolean,
    conversation: Conversation?,
//    isMuted: Boolean,
    showPopup: Boolean
) {
    Column(
        modifier = modifier.statusBarsPadding()
    ) {
        ScreenNameToolbar(
            modifier = modifier,
            screenName = conversation?.name ?: "",
            leftButton = {
                BackButton(modifier = Modifier) {
                    backClick()
                }
            },
            rightButton = {
                if (showMoreButton)
                    MoreButtonWithDropdown(
                        showPopup = showPopup,
//                        isMuted = isMuted,
                        moreClick = moreClick
                    )
                else
                    Box(modifier = Modifier.size(56.dp))
            })
        Text(
            text = pluralStringResource(
                id = R.plurals.x_participants,
                conversation?.participantsCount ?: 0,
                conversation?.participantsCount ?: 0,
            ),
            style = MaterialTheme.typography.bodyMedium.copy(color = gray),
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(bottom = 14.dp)
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun ToolbarForPrivateChat(
    modifier: Modifier = Modifier,
    backClick: () -> Unit = {},
    conversation: Conversation?,
) {
    val user: SimpleUser? = conversation?.users?.firstOrNull()
    Box(modifier = Modifier.statusBarsPadding()) {
        Row(
            modifier = modifier
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackButton(modifier = Modifier) {
                backClick()
            }
            val textSize = remember { mutableStateOf(20.sp) }
            Column(
                modifier = Modifier
                    .align(CenterVertically)
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = conversation?.name ?: "",
                    style = MaterialTheme.typography.headline20.copy(color = gray),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    fontSize = textSize.value,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult ->
                        val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                        if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                            textSize.value = textSize.value.times(0.9f)
                        }
                    },
                )
                Text(
                    text = "", // status text Online/offline missing information for now
                    style = MaterialTheme.typography.body1.copy(color = darkPurple),
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .align(CenterHorizontally)
                )
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight(), contentAlignment = Center
            ) {
                user?.let { user ->
                    CircleAvatarImage(
                        avatar = user.photo?.image5050,
                        displayName = user.displayName,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ToolbarWithNameBack(
    modifier: Modifier = Modifier,
    backClick: () -> Unit,
    screenName: String,
) {
    ScreenNameToolbar(
        modifier = modifier,
        screenName = screenName,
        leftButton = {
            BackButton(modifier = Modifier) {
                backClick()
            }
        },
        rightButton = {
            Box(modifier = Modifier.size(56.dp))
        })
}