@file:OptIn(ExperimentalPagerApi::class)

package com.i2asolutions.athelo.presentation.ui.tutorial

import android.graphics.PointF
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.tutorial.TutorialPageItem
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.ResizeHorizontalPagerIndicator
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.lightPurple
import com.i2asolutions.athelo.presentation.ui.theme.paragraph
import com.i2asolutions.athelo.presentation.ui.theme.purple
import com.i2asolutions.athelo.utils.TutorialWelcomeShape
import kotlin.math.max
import kotlin.math.min

@Composable
fun TutorialScreen(viewModel: TutorialViewModel) {

    val state = viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        modifier = Modifier.navigationBarsPadding()
    ) {
        val pagerState = rememberPagerState()

        Box(
            modifier = Modifier.align(Alignment.BottomEnd),
            contentAlignment = Alignment.TopCenter
        ) {
            ResizeHorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = state.value.pages.size - 1,
                modifier = Modifier.padding(top = 64.dp),
                activeColor = lightPurple.copy(alpha = 1.0f),
                inactiveColor = lightPurple.copy(alpha = 0.5f),
                activeIndicatorHeight = 14.dp,
                activeIndicatorWidth = 14.dp,
                inactiveIndicatorHeight = 10.dp,
                inactiveIndicatorWidth = 10.dp,
                spacing = 8.dp
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                painter = painterResource(id = R.drawable.frame_1640),
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
            )

        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = pagerState.currentPage > 0,
            exit = fadeOut(),
            enter = fadeIn()
        ) {
            Image(
                modifier = Modifier
                    .height(44.dp)
                    .padding(vertical = 6.dp)
                    .align(Alignment.TopCenter),
                painter = rememberAsyncImagePainter(R.drawable.athelo_logo_with_text),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        HorizontalPager(
            count = state.value.pages.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            /*.pointerInput(Unit) {
                detectHorizontalDragGestures { change, dragAmount ->
                    scope.launch {
                        debugPrint(change, dragAmount, state)
                        val delta = dragAmount < 0
                        if (delta)
                            state.animateScrollToPage(
                                state.currentPage + 1,
                                0f
                            )

                    }
                }*/
            /*}*/
            verticalAlignment = Alignment.Top,
            userScrollEnabled = true,
        ) { page ->
            val item = state.value.pages[page]
            if (item is TutorialPageItem.FirstPage) {
                WelcomePage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center), item = item
                )
            } else if (item is TutorialPageItem.TutorialPage) {


                TutorialPageScreen(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center),
                    item = item,
                )
            }
        }

        val pageOffset = remember { mutableStateOf(PointF(0f, 0f)) }
        val pageWidth = LocalConfiguration.current.screenWidthDp

        AnimatedVisibility(
            visible = pagerState.currentPage > 1 || (pagerState.currentPage > 0 && pagerState.currentPageOffset >= 0),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Spacer(modifier = Modifier.height(110.dp))
                Box(
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .width(340.dp)
                        .aspectRatio(1.4f)
                        .clip(TutorialWelcomeShape())
                        .background(lightPurple.copy(alpha = 0.2f))
                ) {
                    Image(
                        painter = painterResource(R.mipmap.welcome),
                        contentDescription = "",
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = pageOffset.value.x
                                translationY = pageOffset.value.y
                            },
                    )
                }
            }
        }

        LaunchedEffect(state) {
            snapshotFlow { pagerState.currentPage }.collect {
                viewModel.handleEvent(TutorialEvent.UpdateScreen(state.value.pages[it]))
            }
        }
        LaunchedEffect(state) {
            snapshotFlow { pagerState.currentPageOffset }.collect {
                calculateImageOffset(pagerState, it, pageOffset, pageWidth)
            }
        }
    }


}

private fun calculateImageOffset(
    state: PagerState,
    it: Float,
    pageOffset: MutableState<PointF>,
    pageWidth: Int
) {
    val yMin = -0.3f
    val yMax = 0.4f
    val xMin = -0.4f
    val xMax = 0.4f
    val yState = max(1f, (state.currentPage + it))
    val y = max(yMin, min(yMax, yMin + (yMax - yMin) * yState / 2))
    val x = min(xMax, max(xMin, xMin + (xMax - xMin) * (state.currentPage - 2 + it)))
    pageOffset.value = PointF(x * pageWidth, y * pageWidth)
}

@Composable
fun WelcomePage(modifier: Modifier, item: TutorialPageItem.FirstPage) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item.bigLogo?.let {
            Image(
                modifier = Modifier.fillMaxWidth(0.7f),
                painter = rememberAsyncImagePainter(it),
                contentDescription = ""
            )
            Spacer(modifier = modifier.height(32.dp))
        }
        item.message?.let {
            Text(
                text = stringResource(id = it),
                style = MaterialTheme.typography.paragraph.copy(
                    color = purple,
                    textAlign = TextAlign.Center
                ),
            )
            Spacer(modifier = modifier.height(32.dp))
        }
        item.buttonLabel?.let {
            TutorialButton(modifier = Modifier, onCLick = {

            }, textRes = it)
        }
        Box(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun TutorialPageScreen(modifier: Modifier, item: TutorialPageItem.TutorialPage) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        item.title?.let {
            Text(
                text = stringResource(id = it),
                modifier = Modifier.heightIn(min = 44.dp),
                style = MaterialTheme.typography.paragraph.copy(
                    color = darkPurple,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            )
        } ?: Spacer(modifier = Modifier.height(32.dp))
//        item.bigLogo?.let {
//            Image(
//                modifier = Modifier.fillMaxWidth(),
//                painter = rememberAsyncImagePainter(it),
//                contentDescription = ""
//            )
//            Spacer(modifier = modifier.height(8.dp))
//        }
        Box(modifier = Modifier.height(100.dp))
    }
}

@Preview
@Composable
fun PreviewTutorialPate() {
    TutorialPageScreen(
        modifier = Modifier, item = TutorialPageItem.TutorialPage(
            bigLogo = R.drawable.athelo_logo_with_text,
        )
    )
}

@Composable
fun TutorialButton(modifier: Modifier, onCLick: () -> Unit, @StringRes textRes: Int) {
    val selected = remember { mutableStateOf(false) }
    Button(
        shape = RoundedCornerShape(20.dp),
        onClick = onCLick,
        modifier = modifier.height(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected.value) darkPurple else lightPurple.copy(alpha = 0.1f)
        )
    ) {
        Text(
            text = stringResource(id = textRes),
            style = MaterialTheme.typography.paragraph,
            color = lightPurple
        )
    }
}

@Preview
@Composable
fun PreviewButtonWithProgress() {
    TutorialButton(
        modifier = Modifier.padding(16.dp),
        onCLick = {},
        textRes = R.string.Swipe_to_get_started,
    )
}