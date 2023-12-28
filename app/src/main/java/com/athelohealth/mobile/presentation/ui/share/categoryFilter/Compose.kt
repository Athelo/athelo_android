@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.share.categoryFilter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.news.Category
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.SecondaryButton
import com.athelohealth.mobile.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FilterScreen(viewModel: CategoryFilterViewModel) {
    val state = viewModel.state.collectAsState()
    BoxScreen(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = black.copy(alpha = 0.3f),
        includeStatusBarPadding = false,
        showProgressProvider = {
            false
        },
        viewModel = viewModel
    ) {
        val configuration = LocalConfiguration.current
        val coroutineScope = rememberCoroutineScope()
        val show = rememberSaveable {
            mutableStateOf(false)
        }
        LaunchedEffect(key1 = true) {
            coroutineScope.launch {
                delay(300)
                show.value = true
            }
        }
        AnimatedVisibility(
            visible = show.value,
            enter = fadeIn() + slideInVertically(
                initialOffsetY = { configuration.screenHeightDp },
                animationSpec = tween(300, easing = LinearOutSlowInEasing)
            ),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Card(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                colors = CardDefaults.cardColors(containerColor = white),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.Search_by_topic),
                    style = MaterialTheme.typography.headline20.copy(color = darkPurple),
                    modifier = Modifier.padding(16.dp)
                )
                LazyColumn {
                    items(state.value.allFilters, key = { item: Category -> item.id }) {
                        ChooseOptionRow(
                            category = it,
                            selectionProvider = { state.value.selectedFilters.contains(it) },
                            handleEvent = viewModel::handleEvent
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SecondaryButton(
                        text = R.string.Cancel,
                        onClick = { viewModel.handleEvent(CategoryFilterEvent.CancelClick) },
                        background = white,
                        border = darkPurple,
                        modifier = Modifier.width(140.dp),
                        textPadding = PaddingValues(horizontal = 0.dp)
                    )
                    MainButton(
                        textId = R.string.Search,
                        modifier = Modifier.width(140.dp),
                        onClick = {
                            viewModel.handleEvent(CategoryFilterEvent.SearchClick)
                        })
                }
            }
        }

    }
}

@Composable
fun ChooseOptionRow(
    category: Category,
    selectionProvider: () -> Boolean,
    handleEvent: (CategoryFilterEvent) -> Unit
) {
    val checked = remember { mutableStateOf(selectionProvider()) }
    Row(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clickable {
            checked.value = !checked.value
            handleEvent(CategoryFilterEvent.CategoryClick(category))
        }) {
        Image(
            painter = painterResource(id = if (checked.value) R.drawable.checkbox_on else R.drawable.checkbox_off),
            contentDescription = "",
            modifier = Modifier.padding(end = 24.dp),
        )
        Text(text = category.name, style = MaterialTheme.typography.textField.copy(color = gray))
    }
}