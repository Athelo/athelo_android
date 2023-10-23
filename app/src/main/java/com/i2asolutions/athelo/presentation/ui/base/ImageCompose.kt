package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.ui.theme.background
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.headline20
import com.i2asolutions.athelo.presentation.ui.theme.headline30


@Composable
fun ImageWithProgress(
    image: String?,
    modifier: Modifier,
    contentScale: ContentScale = ContentScale.FillWidth
) {
    val cache by remember { mutableStateOf(image) }
    Box(modifier) {
        if (cache.isNullOrBlank()) {
            Image(
                painter = painterResource(id = R.drawable.placeholder),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Image(
                painter = painterResource(id = R.drawable.athelo_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        } else {
            val showImageLoading = remember { mutableStateOf(false) }
            AnimatedVisibility(
                visible = showImageLoading.value,
                modifier = Modifier.fillMaxSize(),
                enter = fadeIn(),
                exit = fadeOut(tween(easing = FastOutLinearInEasing))
            ) {
                Box {
                    CircleProgress(modifier = Modifier.align(Alignment.Center))
                }
            }
            Image(
                painter = rememberAsyncImagePainter(model = cache, onLoading = {
                    showImageLoading.value = true
                }, onSuccess = {
                    showImageLoading.value = false
                }),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = contentScale
            )

        }
    }
}

@Composable
fun CircleAvatarImage(
    modifier: Modifier = Modifier.size(32.dp),
    avatar: String?,
    displayName: String,
    borderColor: Color = darkPurple,
    includeBorder: Boolean = false,
    borderWidth: Dp = 1.dp
) {
    if (avatar.isNullOrBlank()) {
        val names = displayName.split(" ")
        val nameLetter = when {
            names.size >= 2 -> "${names[0].firstOrNull() ?: ""}${names[1].firstOrNull() ?: ""}"
            names.size == 1 -> "${names[0].firstOrNull() ?: ""}"
            else -> ""
        }.also { debugPrint(it) }
        Box(
            modifier = modifier
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = CircleShape
                )
                .background(background, CircleShape)
                .clip(CircleShape)
        ) {
            Text(
                text = nameLetter.uppercase(),
                style = MaterialTheme.typography.headline20.copy(
                    color = darkPurple,
                ),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(borderWidth)
            )
        }
    } else {
        Image(
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "",
            modifier = modifier
                .composed {
                    if (includeBorder)
                        Modifier.border(borderWidth, color = borderColor, shape = CircleShape)
                    else Modifier
                }
                .clip(CircleShape)
                .background(background)

        )
    }
}

@Composable
fun RoundedCornerAvatarImage(
    modifier: Modifier = Modifier,
    avatar: String?,
    displayName: String,
    shape: Shape = RoundedCornerShape(25.dp)
) {
    if (avatar.isNullOrBlank()) {
        val names = displayName.split(" ")
        val nameLetter = when {
            names.size >= 2 -> "${names[0].firstOrNull() ?: ""}${names[1].firstOrNull() ?: ""}"
            names.size == 1 -> "${names[0].firstOrNull() ?: ""}"
            else -> ""
        }.also { debugPrint(it) }
        Box(
            modifier = modifier
                .size(88.dp)
                .background(background, shape = shape)
                .border(
                    border = BorderStroke(1.dp, darkPurple),
                    shape = shape
                )
                .clip(shape = shape)
        ) {
            Text(
                text = nameLetter.uppercase(),
                style = MaterialTheme.typography.headline30.copy(
                    color = darkPurple,
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(2.dp)
            )
        }
    } else {
        Image(
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "",
            modifier = modifier
                .size(88.dp)
                .clip(shape = shape)
                .background(background),
        )
    }
}