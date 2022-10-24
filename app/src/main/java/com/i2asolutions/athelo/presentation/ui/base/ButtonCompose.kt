package com.i2asolutions.athelo.presentation.ui.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.model.chat.ChatMoreButtonClickAction
import com.i2asolutions.athelo.presentation.ui.theme.*

@Composable
fun SecondaryButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
    onClick: () -> Unit,
    background: Color,
    border: Color = background,
    textColor: Color = border,
    textStyle: TextStyle = MaterialTheme.typography.button.copy(
        color = textColor,
        textAlign = TextAlign.Center
    ),
    textPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    showShadow: Boolean = true
) {
    var lastClickTimestamp by remember { mutableStateOf(0L) }
    Button(
        onClick = {
            if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                lastClickTimestamp = System.currentTimeMillis()
                onClick()
            }
        },
        modifier = modifier
            .shadow(if (showShadow) 3.dp else 0.dp, shape = CircleShape, ambientColor = border)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        border = BorderStroke(1.dp, border),
    ) {
        var textSize by remember { mutableStateOf(16.sp) }
        Text(
            modifier = Modifier
                .padding(textPadding)
                .align(Alignment.CenterVertically),
            text = stringResource(id = text),
            style = textStyle.copy(fontSize = textSize),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1
                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    textSize = textSize.times(0.9f)
                }
            },
        )
    }
}

@Composable
fun SecondaryWithImageButton(
    @DrawableRes imageRes: Int,
    @StringRes textRes: Int,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
    onClick: () -> Unit,
    background: Color,
    border: Color = background,
    textColor: Color = border,
    textStyle: TextStyle = MaterialTheme.typography.button.copy(
        color = textColor,
        textAlign = TextAlign.Center
    ),
    showShadow: Boolean = true
) {
    var lastClickTimestamp by remember { mutableStateOf(0L) }
    Button(
        onClick = {
            if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                lastClickTimestamp = System.currentTimeMillis()
                onClick()
            }
        },
        modifier = modifier
            .shadow(if (showShadow) 3.dp else 0.dp, shape = CircleShape, ambientColor = border)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = background
        ),
        border = BorderStroke(1.dp, border),
    ) {
        Image(
            modifier = Modifier
                .width(32.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = imageRes),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .padding(
                    end = 0.dp,
                    start = 0.dp
                )
                .weight(1f)
                .align(Alignment.CenterVertically),
            text = stringResource(id = textRes),
            style = textStyle,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun RoundButtonPreview() {
    SecondaryWithImageButton(
        imageRes = R.drawable.login_with,
        textRes = R.string.Sign_up_with_email,
        modifier = Modifier,
        onClick = { /*TODO*/ },
        background = white,
        border = purple
    )
}

@Composable
fun TosPpText(modifier: Modifier = Modifier, onTosClick: () -> Unit, onPPClick: () -> Unit) {
    val text = "By signing Up, you agree with the\n Terms of Service and Privacy Policy"
    val tosText = "Terms of Service"
    val ppText = "Privacy Policy"
    val startToSText = text.indexOf(tosText)
    val startPPText = text.indexOf(ppText)

    val annotatedText = buildAnnotatedString {

        append(text)
        addStringAnnotation(
            "URL",
            "https://google.com",
            start = startToSText,
            startToSText + tosText.length
        )
        addStyle(
            MaterialTheme.typography.body1.toSpanStyle(),
            startToSText,
            startToSText + tosText.length
        )
        addStringAnnotation(
            "URL",
            "https://google.com",
            start = startPPText,
            startPPText + ppText.length
        )
        addStyle(
            MaterialTheme.typography.body1.toSpanStyle(),
            startPPText,
            startPPText + ppText.length
        )
    }
    ClickableText(
        text = annotatedText,
        style = MaterialTheme.typography.body1.copy(gray, textAlign = TextAlign.Center),
        modifier = modifier.padding(bottom = 8.dp),
        onClick = {
            if (it in startToSText until startToSText + tosText.length) {
                onTosClick()
            } else if (it in startPPText until startPPText + ppText.length) {
                onPPClick()
            }
            debugPrint(it, startPPText, startToSText)
        },
        overflow = TextOverflow.Visible
    )
}


@Composable
fun MainButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    textId: Int,
    enableButton: Boolean = true,
    showShadow: Boolean = true,
    onClick: () -> Unit,
    background: Color = darkPurple,
) {
    var lastClickTimestamp by remember { mutableStateOf(0L) }
    Button(
        modifier = Modifier
            .composed { modifier }
            .shadow(
                if (enableButton && showShadow) 3.dp else 0.dp,
                CircleShape,
                ambientColor = background.copy(alpha = 0.08f)
            )
            .height(52.dp),
        onClick = {
            if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                lastClickTimestamp = System.currentTimeMillis()
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Transparent,
            containerColor = if (enableButton) background else background.copy(alpha = 0.5f)
        )
    ) {
        var textSize by remember { mutableStateOf(16.sp) }
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.button.copy(color = white, fontSize = textSize),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val maxCurrentLineIndex: Int = textLayoutResult.lineCount - 1

                if (textLayoutResult.isLineEllipsized(maxCurrentLineIndex)) {
                    textSize = textSize.times(0.9f)
                }
            },
        )
    }
}

@Composable
fun RedButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
    textId: Int,
    enableButton: Boolean,
    onClick: () -> Unit
) {
    var lastClickTimestamp by remember { mutableStateOf(0L) }
    Button(
        modifier = Modifier
            .composed { modifier }
            .shadow(
                if (enableButton) 3.dp else 0.dp,
                CircleShape,
                ambientColor = red.copy(alpha = 0.08f)
            )
            .height(52.dp),
        onClick = {
            if (1000L + lastClickTimestamp < System.currentTimeMillis()) {
                lastClickTimestamp = System.currentTimeMillis()
                onClick()
            }
        },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Transparent,
            containerColor = if (enableButton) red else red.copy(
                alpha = 0.5f
            )
        )
    ) {
        val textSize = remember { mutableStateOf(16.sp) }
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.button.copy(color = white),
            maxLines = 1,
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
fun BackButton(
    modifier: Modifier,
    onBackClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.ic_back_arrow),
        contentDescription = "Back Button",
        modifier = modifier
            .size(56.dp)
            .clickable(onClick = onBackClick),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun FavouriteButton(
    modifier: Modifier,
    initFavouriteState: () -> Boolean,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = if (initFavouriteState()) R.drawable.favorite_on else R.drawable.favorite_off),
        contentDescription = "Back Button",
        modifier = modifier
            .size(56.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun MoreButton(modifier: Modifier, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.ic_more_vertical),
        contentDescription = "More Button",
        modifier = modifier
            .size(56.dp)
            .clickable(onClick = onClick),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {
    Image(
        painter = painterResource(id = R.drawable.ic_menu),
        contentDescription = "Menu Button",
        modifier = modifier
            .size(56.dp)
            .clickable(onClick = onMenuClick),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun MoreButtonWithDropdown(
    showPopup: Boolean,
//    isMuted: Boolean,
    moreClick: (ChatMoreButtonClickAction) -> Unit
) {
    Box {
        MoreButton(
            modifier = Modifier,
            onClick = { moreClick(ChatMoreButtonClickAction.ShowPopup) })
        DropdownMenu(
            expanded = showPopup,
            cornerRadius = 12.dp,
            offset = DpOffset(8.dp, 0.dp),
            onDismissRequest = { moreClick(ChatMoreButtonClickAction.DismissPopup) },
            content = {
                Column {
//                    if (isMuted)
//                        MoreButtonWithDropdownRow(
//                            { moreClick(ChatMoreButtonClickAction.UnMute) },
//                            R.drawable.ic_mute_off,
//                            R.string.Unmute,
//                            addTopPadding = false,
//                            addBottomPadding = true
//                        )
//                    else
//                        MoreButtonWithDropdownRow(
//                            { moreClick(ChatMoreButtonClickAction.Mute) },
//                            R.drawable.ic_mute_on,
//                            R.string.Mute,
//                            addTopPadding = false,
//                            addBottomPadding = true
//                        )
//                    Divider(color = lightGray, thickness = 1.dp)
                    MoreButtonWithDropdownRow(
                        { moreClick(ChatMoreButtonClickAction.Leave) },
                        R.drawable.ic_leave,
                        R.string.Leave,
                        addTopPadding = false,
                        addBottomPadding = false
                    )
                }
            })
    }
}

@Composable
fun MoreButtonWithDropdownRow(
    clickListener: () -> Unit,
    @DrawableRes icon: Int,
    @StringRes text: Int,
    addTopPadding: Boolean,
    addBottomPadding: Boolean
) {
    Row(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = if (addTopPadding) 16.dp else 8.dp,
                bottom = if (addBottomPadding) 16.dp else 8.dp
            )
            .clickable(onClick = clickListener)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = R.string.app_name)
        )
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(id = text),
            style = MaterialTheme.typography.button.copy(color = purple)
        )
    }
}

@Preview
@Composable
fun MainButtonEnPrev() {
    MainButton(textId = R.string.Log_In, enableButton = true, onClick = {})
}

@Preview(backgroundColor = 0xFFFFFFFF)
@Composable
fun MainButtonDisPrev() {
    MainButton(textId = R.string.Log_In, enableButton = false, onClick = {})
}