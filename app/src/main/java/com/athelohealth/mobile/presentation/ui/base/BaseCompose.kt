package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
internal fun InfiniteTransition.fraction(
    durationMillis: Int,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): State<Float> {
    val duration = durationMillis + delayMillis

    return animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = duration
                0f at delayMillis / 2 with easing
                1f at durationMillis + (delayMillis / 2)
                1f at duration
            }
        )
    )
}

@Composable
internal fun InfiniteTransition.fraction2Step(
    durationMillis: Int,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): State<Float> {
    val duration = durationMillis + delayMillis

    return animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = duration
                0f at delayMillis / 2 with easing
                .5f at duration / 2
                .5f at (duration + delayMillis) / 2 with easing
                1f at duration with easing
            }
        )
    )
}

@Composable
internal fun InfiniteTransition.fraction2StepReversed(
    durationMillis: Int,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): State<Float> {
    val duration = durationMillis + delayMillis

    return animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                this.durationMillis = duration
                0f at delayMillis / 2 with easing
                1f at duration / 2
                1f at (duration + delayMillis) / 2 with easing
                0f at duration
            }
        )
    )
}
