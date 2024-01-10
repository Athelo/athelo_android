@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.ui.theme.*
import com.athelohealth.mobile.utils.conectivity.NetWorkManager
import com.athelohealth.mobile.utils.consts.Const
import com.athelohealth.mobile.utils.getShiftedFraction
import com.athelohealth.mobile.utils.lerp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BoxScreen(
    modifier: Modifier = Modifier,
    viewModel: BaseViewModel<*, *, *>?,
    showProgressProvider: () -> Boolean,
    backgroundColor: Color = background,
    includeStatusBarPadding: Boolean = true,
    content: @Composable @UiComposable BoxScope.() -> Unit
) {
    val errorState = viewModel?.errorStateFlow?.collectAsState(
        initial = if (NetWorkManager.isDisconnected) MessageState.LowConnectivityMessageState(Const.NO_INTERNET_MESSAGE)
        else MessageState.NoMessageState()
    )
    Box(
        modifier = modifier
            .background(backgroundColor)
            .composed {
                if (includeStatusBarPadding)
                    Modifier.statusBarsPadding()
                else Modifier
            }
            .fillMaxSize(),
    ) {
        content()
        debugPrint(errorState)
        errorState?.value.let { error ->
            AnimatedVisibility(
                modifier = Modifier.composed {
                    if (!includeStatusBarPadding)
                        Modifier.statusBarsPadding()
                    else
                        Modifier
                },
                visible = error !is MessageState.NoMessageState,
                enter = slideInVertically(),
                exit = slideOutVertically()
            ) {
                when (error) {
                    is MessageState.ErrorMessageState -> ErrorMessage(error = error) {
                        viewModel?.clearError()
                    }
                    is MessageState.SuccessMessageState -> SuccessMessage(error = error) {
                        viewModel?.clearError()
                    }
                    is MessageState.LowConnectivityMessageState -> TopErrorMessage(error = error)
                    is MessageState.NoMessageState -> {}
                    is MessageState.NormalMessageState -> TopMessage(error = error) {
                        viewModel?.clearError()
                    }
                    null -> {

                    }
                }
            }
        }
        if (errorState?.value is MessageState.NoMessageState && showProgressProvider()) LoadingPopup()
    }
}

@Composable
fun LoadingPopup() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(black.copy(alpha = 0.36f))
            .padding(16.dp)
            .clickable(indication = null, interactionSource = MutableInteractionSource()) {

            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(all = 16.dp)
                .shadow(0.dp)
                .fillMaxWidth()
                .aspectRatio(1.0f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = white),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.athelo_logo_with_text),
                contentDescription = "",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.Loading),
                style = MaterialTheme.typography.button.copy(color = darkPurple),
                color = darkPurple
            )
            Spacer(modifier = Modifier.height(8.dp))
            BallSpinFadeLoaderProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private const val DefaultAnimationDuration = 1000

private val DefaultMaxBallDiameter = 7.2.dp
private val DefaultMinBallDiameter = 7.2.dp
private val DefaultDiameter = 40.dp

private const val DefaultMaxAlpha = 1f
private const val DefaultMinAlpha = .4f

@Composable
fun BallSpinFadeLoaderProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    animationDuration: Int = DefaultAnimationDuration,
    maxBallDiameter: Dp = DefaultMaxBallDiameter,
    minBallDiameter: Dp = DefaultMinBallDiameter,
    diameter: Dp = DefaultDiameter,
    maxAlpha: Float = DefaultMaxAlpha,
    minAlpha: Float = DefaultMinAlpha,
    isClockwise: Boolean = false
) {
    val transition = rememberInfiniteTransition()
    val fraction by transition.fraction(animationDuration)

    Canvas(
        modifier = modifier
            .progressSemantics()
            .size(diameter)
            .focusable(),
    ) {
        val diameterList = mutableListOf<Float>()
        val alphaList = mutableListOf<Float>()
        for (i in 0..7) {
            val newFraction =
                getShiftedFraction(if (isClockwise) 1 - fraction else fraction, i * .1f)
            lerp(minBallDiameter, maxBallDiameter, newFraction).also { diameterList.add(it.toPx()) }
            lerp(minAlpha, maxAlpha, newFraction).also { alphaList.add(it) }
        }

        drawIndeterminateBallSpinFadeLoaderIndicator(
            maxDiameter = maxBallDiameter.toPx(),
            alpha = alphaList,
            diameter = diameterList,
            color = color
        )
    }
}

private fun DrawScope.drawIndeterminateBallSpinFadeLoaderIndicator(
    maxDiameter: Float,
    diameter: List<Float>,
    alpha: List<Float>,
    color: Color
) {
    for (i in 0..7) {
        val radius = diameter[i] / 2
        val angle = PI.toFloat() / 4 * i
        val x = (size.width - maxDiameter) * cos(angle) / 2
        val y = (size.height - maxDiameter) * sin(angle) / 2

        drawCircle(
            color = color,
            radius = radius,
            center = center + Offset(x, y),
            alpha = alpha[i]
        )
    }
}