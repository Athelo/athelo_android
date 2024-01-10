@file:OptIn(ExperimentalPagerApi::class)

package com.athelohealth.mobile.presentation.ui.share.authorization.landing

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.SecondaryWithImageButton
import com.athelohealth.mobile.presentation.ui.base.TosPpText
import com.athelohealth.mobile.presentation.ui.theme.*

@Composable
private fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition,
    width: Dp
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "customTabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - width) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .background(purple, CircleShape)
}

@Composable
fun AuthorizationLandingScreen(viewModel: AuthorizationLandingViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier.navigationBarsPadding(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val tabs =
                listOf(stringResource(id = R.string.Sign_Up), stringResource(id = R.string.Log_In))
            TabRow(
                modifier = Modifier.padding(top = 56.dp),
                selectedTabIndex = viewState.value.selectedTabIndex,
                containerColor = background,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.customTabIndicatorOffset(
                            tabPositions[viewState.value.selectedTabIndex],
                            100.dp
                        )
                    )
                },
                divider = {
                    TabRowDefaults.Divider(color = Color.Transparent)
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    val selected = viewState.value.selectedTabIndex == index
                    Tab(
                        selected = selected,
                        onClick = {
                            viewModel.handleEvent(AuthorizationLandingEvent.TabClick(index))
                        }) {
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.height(56.dp),
                            text = title,
                            style = MaterialTheme.typography.headline24.copy(color = gray.copy(alpha = if (selected) 1.0f else 0.5f))
                        )
                    }
                }
            }

            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
//                androidx.compose.animation.AnimatedVisibility(
//                    visible = state.value.selectedTabIndex == 0,
//                    enter = slideInHorizontally(
//                        initialOffsetX = { -it },
//                        animationSpec = tween(300, easing = LinearEasing, delayMillis = 100)
//                    ),
//                    exit = slideOutHorizontally(
//                        targetOffsetX = { -it },
//                        animationSpec = tween(300, easing = LinearEasing)
//                    )
//                )
                if (viewState.value.selectedTabIndex == 0) {
                    SignUpOptionScreen(viewModel = viewModel, Modifier)
                }
//                androidx.compose.animation.AnimatedVisibility(
//                    visible = state.value.selectedTabIndex == 1,
//                    enter = slideInHorizontally(
//                        initialOffsetX = { it },
//                        animationSpec = tween(300, easing = LinearEasing, delayMillis = 100)
//                    ),
//                    exit = slideOutHorizontally(
//                        targetOffsetX = { it },
//                        animationSpec = tween(300, easing = LinearEasing)
//                    )
//                )
                else if (viewState.value.selectedTabIndex == 1) {
                    SignInOptionScreen(viewModel = viewModel, Modifier)
                }
            }
            TosPpText(onPPClick = {
                viewModel.handleEvent(AuthorizationLandingEvent.PPLinkClick)
            }, onTosClick = {
                viewModel.handleEvent(AuthorizationLandingEvent.TosLinkClick)
            })
        }
    }
}

@Composable
private fun SignInOptionScreen(viewModel: AuthorizationLandingViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SecondaryWithImageButton(
            imageRes = R.drawable.login_with,
            textRes = R.string.Sign_in_with_email,
            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignInWithEmailClick) },
            background = white,
            border = lightPurple,
            textStyle = MaterialTheme.typography.button.copy(color = purple)
        )
        Spacer(modifier = Modifier.height(24.dp))
        SecondaryWithImageButton(
            imageRes = R.drawable.google,
            textRes = R.string.Sign_in_with_Google,
            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithGoogleClick) },
            background = white,
            border = black,
            textStyle = MaterialTheme.typography.button.copy(color = black)
        )
        Spacer(modifier = Modifier.height(24.dp))
//        RoundButton(
//            image = R.drawable.apple,
//            text = R.string.Sign_in_with_Apple,
//            modifier = Modifier,
//            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithAppleClick) },
//            background = white,
//            border = black,
//            textStyle = MaterialTheme.typography.button.copy(color = black)
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//        SecondaryWithImageButton(
//            imageRes = R.drawable.facebook,
//            textRes = R.string.Sign_in_with_Facebook,
//            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithFacebookClick) },
//            background = white,
//            border = black,
//            textStyle = MaterialTheme.typography.button.copy(color = black)
//        )
//        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Composable
private fun SignUpOptionScreen(viewModel: AuthorizationLandingViewModel, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SecondaryWithImageButton(
            imageRes = R.drawable.login_with,
            textRes = R.string.Sign_up_with_email,
            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignUpWithEmailClick) },
            background = white,
            border = lightPurple,
            textStyle = MaterialTheme.typography.button.copy(color = purple)
        )
        Spacer(modifier = Modifier.height(24.dp))
        SecondaryWithImageButton(
            imageRes = R.drawable.google,
            textRes = R.string.Sign_up_with_Google,
            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithGoogleClick) },
            background = white,
            border = black,
            textStyle = MaterialTheme.typography.button.copy(color = black)
        )
        Spacer(modifier = Modifier.height(24.dp))
//        RoundButton(
//            image = R.drawable.apple,
//            text = R.string.Sign_up_with_Apple,
//            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithAppleClick) },
//            background = white,
//            border = black,
//            textStyle = MaterialTheme.typography.button.copy(color = black)
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//        SecondaryWithImageButton(
//            imageRes = R.drawable.facebook,
//            textRes = R.string.Sign_up_with_Facebook,
//            onClick = { viewModel.handleEvent(AuthorizationLandingEvent.SignWithFacebookClick) },
//            background = white,
//            border = black,
//            textStyle = MaterialTheme.typography.button.copy(color = black)
//        )
//        Spacer(modifier = Modifier.height(24.dp))
    }
}