@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.share.askAthelo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.application.FAQ
import com.i2asolutions.athelo.presentation.model.askAthelo.AskAtheloSection
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.MainButton
import com.i2asolutions.athelo.presentation.ui.base.Toolbar
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.widgets.HtmlText

@Composable
fun AskAtheloScreen(viewModel: AskAtheloViewModel) {
    val state = viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.value.isLoading },
        modifier = Modifier.navigationBarsPadding()
    ) {
        Content(handleEvent = viewModel::handleEvent, questions = state.value.questions)
    }
}

@Composable
private fun Content(
    questions: List<AskAtheloSection>,
    handleEvent: (AskAtheloEvent) -> Unit = {},
) {
    Column {
        Toolbar(
            screenName = stringResource(id = R.string.Ask_Athelo),
            onBackClick = { handleEvent(AskAtheloEvent.BackButtonClick) },
            showBack = true
        )
        LazyColumn(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.Ask_Athelo_header),
                    style = MaterialTheme.typography.paragraph.copy(
                        color = darkPurple,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 44.dp)
                        .padding(bottom = 24.dp)
                        .fillMaxWidth()
                )
            }

            items(questions, key = { it.faq.hashCode() }) { question ->
                QuestionSection(question = question, onEvent = handleEvent)
            }
            item {
                Text(
                    text = stringResource(id = R.string.Still_have_more_question),
                    style = MaterialTheme.typography.link.copy(
                        color = gray,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 44.dp)
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                )
            }
            item {
                MainButton(
                    textId = R.string.Send_Message,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    onClick = {
                        handleEvent(AskAtheloEvent.SendFeedbackClick)
                    })
            }
        }
    }
}

@Composable
fun QuestionSection(question: AskAtheloSection, onEvent: (AskAtheloEvent) -> Unit) {
    ConstraintLayout(modifier = Modifier) {
        val rotationValue = if (question.expanded) 180f else 0f
        val expandState = question.expanded
        val (header, content) = createRefs()
        AnimatedVisibility(modifier = Modifier
            .constrainAs(content) {
                top.linkTo(header.bottom, margin = (-20).dp)
            }
            .absoluteOffset(y = (-20).dp),
            visible = expandState,
            enter = fadeIn(animationSpec = tween(100, easing = LinearOutSlowInEasing)),
            exit = fadeOut(animationSpec = tween(100, easing = LinearOutSlowInEasing))) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 24.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = white),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 18.dp
                ),
            ) {
                HtmlText(
                    text = question.faq.content,
                    style = MaterialTheme.typography.body1.copy(color = gray),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp),
                    linkClicked = { onEvent(AskAtheloEvent.LinkClicked(it)) }
                )
            }
        }
        Card(
            modifier = Modifier
                .constrainAs(header) {
                    top.linkTo(parent.top)
                }
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = white),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp,
                pressedElevation = 18.dp
            ),
            onClick = {
                onEvent(AskAtheloEvent.SelectQuestion(question))
            },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 80.dp)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                HtmlText(
                    text =
                    question.faq.header,
                    style = MaterialTheme.typography.body1.copy(color = gray),
                    modifier = Modifier
                        .weight(1f),
                )
                Image(
                    painter = painterResource(id = R.drawable.arrows),
                    contentDescription = null,
                    modifier = Modifier
                        .size(22.dp)
                        .rotate(rotationValue)
                )
            }
        }

    }
}

@Preview
@Composable
fun ContentPreview() {
    Content(
        questions = listOf(
            AskAtheloSection(FAQ("<h2>Test Question</h2>", "Test text"), false),
            AskAtheloSection(FAQ("Test Question", "Test text"), false),
            AskAtheloSection(FAQ("Test Question", "Test text"), false)
        )
    )
}