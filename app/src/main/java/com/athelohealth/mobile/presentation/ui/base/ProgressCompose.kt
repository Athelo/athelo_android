package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.presentation.ui.theme.lightPurple
import kotlinx.coroutines.delay


@Composable
fun CircleProgress(
    modifier: Modifier,
    circleSize: Dp = 7.dp,
    circleColor: Color = MaterialTheme.colorScheme.primary,
    spaceBetween: Dp = 5.dp,
) {
    val circles = listOf(
        remember { Animatable(initialValue = circleColor.copy(alpha = 0.2f)) },
        remember { Animatable(initialValue = circleColor.copy(alpha = 0.2f)) },
        remember { Animatable(initialValue = circleColor.copy(alpha = 0.2f)) },
        remember { Animatable(initialValue = circleColor.copy(alpha = 0.2f)) },
    )
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 300L)
            animatable.animateTo(
                targetValue = lightPurple.copy(alpha = 1f),
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        circleColor.copy(alpha = 0.2f) at 300 with LinearOutSlowInEasing
                        circleColor.copy(alpha = 0.4f) at 600 with LinearOutSlowInEasing
                        circleColor.copy(alpha = 0.6f) at 900 with LinearOutSlowInEasing
                        circleColor.copy(alpha = 1.0f) at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }
    val circleValues = circles.map { it.value }
    val lastCircle = circleValues.size - 1
    Row(modifier = modifier) {
        circleValues.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .background(color, CircleShape)
            )
            if (index != lastCircle) Spacer(modifier = Modifier.width(spaceBetween))
        }
    }
}
