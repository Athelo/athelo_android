@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.theme.*
import java.lang.Float.min

@Composable
fun ConfirmPopup(
    title: String,
    description: String,
    confirmButtonText: Int = R.string.Log_Out,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(black.copy(alpha = 0.36f))
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(size = 30.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 40.dp)
                    .height(42.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headline20.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.paragraph.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 40.dp)
            )
            Row(
                Modifier
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton(
                    text = R.string.Cancel,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 16.dp, end = 8.dp),
                    onClick = onCancelClick,
                    background = white, border = purple
                )
                MainButton(
                    textId = confirmButtonText,
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(start = 8.dp, end = 16.dp),
                    onClick = onConfirmClick,
                    enableButton = true
                )
            }
        }
    }
}

@Composable
fun DeletePopup(
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    title: String,
    description: String
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(black.copy(alpha = 0.36f))
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            shape = RoundedCornerShape(size = 30.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 40.dp)
                    .height(42.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headline20.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.paragraph.copy(
                    darkPurple, textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 40.dp)
            )
            Row(
                Modifier
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SecondaryButton(
                    text = R.string.Cancel,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(0.5f),
                    onClick = onCancelClick,
                    background = white, border = purple
                )
                RedButton(
                    textId = R.string.Delete,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(0.5f),
                    onClick = onConfirmClick,
                    enableButton = true
                )
            }
        }
    }
}

@Composable
fun TopErrorMessage(error: MessageState) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .shadow(10.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white),
    ) {
        Box {
            Image(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.frame_3741),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(69.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.athelo_logo),
                    contentDescription = "Athelo"
                )
                Text(
                    text = error.message.trim(),
                    style = MaterialTheme.typography.body1,
                    color = gray
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(error: MessageState, onCloseClick: () -> Unit) {
    val offsetY = remember { mutableStateOf(0.0f) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged {
                size.value = it
            }
            .graphicsLayer {
                translationY = offsetY.value
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState() {
                    val newValue = min(0f, offsetY.value + it)
                    offsetY.value = newValue
                },
                onDragStopped = {
                    onCloseClick()
                }
            )
            .shadow(10.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white),
    ) {

        Box {
            Image(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.frame_3741),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(69.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.athelo_logo),
                    contentDescription = "Athelo"
                )
                Text(
                    text = error.message.trim(),
                    style = MaterialTheme.typography.body1,
                    color = red,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(16.dp)
                        .clickable { onCloseClick() },
                    painter = painterResource(id = R.drawable.fi_sr_cross_small),
                    contentDescription = "Athelo",
                    colorFilter = ColorFilter.tint(gray)
                )
            }
        }
    }
}

@Composable
fun SuccessMessage(error: MessageState, onCloseClick: () -> Unit) {
    val offsetY = remember { mutableStateOf(0.0f) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged {
                size.value = it
            }
            .graphicsLayer {
                translationY = offsetY.value
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState {
                    val newValue = min(0f, offsetY.value + it)
                    offsetY.value = newValue
                },
                onDragStopped = {
                    onCloseClick()
                }
            )
            .shadow(10.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white),
    ) {

        Box {
            Image(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.frame_3741),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(69.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.athelo_logo),
                    contentDescription = "Athelo"
                )
                Text(
                    text = error.message.trim(),
                    style = MaterialTheme.typography.body1,
                    color = green,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(16.dp)
                        .clickable { onCloseClick() },
                    painter = painterResource(id = R.drawable.fi_sr_cross_small),
                    contentDescription = "Athelo",
                    colorFilter = ColorFilter.tint(gray)
                )
            }
        }
    }
}

@Composable
fun TopMessage(error: MessageState, onCloseClick: () -> Unit) {
    val offsetY = remember { mutableStateOf(0.0f) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged {
                size.value = it
            }
            .graphicsLayer {
                translationY = offsetY.value
            }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState {
                    val newValue = min(0f, offsetY.value + it)
                    offsetY.value = newValue
                },
                onDragStopped = {
                    onCloseClick()
                }
            )
            .shadow(10.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = white),
    ) {

        Box {
            Image(
                modifier = Modifier.align(Alignment.BottomEnd),
                painter = painterResource(id = R.drawable.frame_3741),
                contentScale = ContentScale.FillHeight,
                contentDescription = ""
            )
            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(69.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.athelo_logo),
                    contentDescription = "Athelo"
                )
                Text(
                    text = error.message,
                    style = MaterialTheme.typography.body1,
                    color = gray,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    modifier = Modifier
                        .size(56.dp)
                        .padding(16.dp)
                        .clickable { onCloseClick() },
                    painter = painterResource(id = R.drawable.fi_sr_cross_small),
                    contentDescription = "Athelo",
                    colorFilter = ColorFilter.tint(gray)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTopError() {
    TopErrorMessage(error = MessageState.LowConnectivityMessageState("Test with long\n4 lines of the code\n 3rd line start\n4th line start "))
}

@Preview
@Composable
fun PreviewError() {
    ErrorMessage(error = MessageState.LowConnectivityMessageState("\"Test with long\\n4 lines of the code\\n 3rd line start\\n4th line start \""), onCloseClick = {})
}