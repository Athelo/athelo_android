package com.i2asolutions.athelo.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.settings.SettingButton
import com.i2asolutions.athelo.presentation.ui.base.BoxScreen
import com.i2asolutions.athelo.presentation.ui.base.ToolbarWithNameBack
import com.i2asolutions.athelo.presentation.ui.theme.button
import com.i2asolutions.athelo.presentation.ui.theme.lightGray

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(viewModel = viewModel, showProgressProvider = { state.isLoading }) {
        Column(Modifier.navigationBarsPadding()) {
            ToolbarWithNameBack(
                backClick = { viewModel.handleEvent(SettingsEvent.BackButtonClick) },
                screenName = stringResource(id = R.string.Settings)
            )
            LazyColumn {
                items(items = state.buttons, key = { item -> item.name }) { button ->
                    when (button) {
                        is SettingButton.SimpleSettingButton -> SimpleButtonCell(
                            button,
                            viewModel::handleEvent
                        )
                        is SettingButton.CheckBoxSettingButton -> {/*Ignore for now - added for Notification */}
                    }

                    Divider(color = lightGray.copy(alpha = 0.2f))
                }
            }
        }
    }
}

@Composable
fun SimpleButtonCell(
    settingButton: SettingButton.SimpleSettingButton,
    handleEvent: (SettingsEvent) -> Unit
) {
    Text(
        text = settingButton.name,
        style = MaterialTheme.typography.button.copy(
            color = Color.Gray,
            textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .clickable {
                handleEvent(SettingsEvent.ButtonClick(settingButton.deeplink))
            }
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun CheckBoxButtonCell(
    settingButton: SettingButton.SimpleSettingButton,
    handleEvent: (SettingsEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clickable {
                handleEvent(SettingsEvent.ButtonClick(settingButton.deeplink))
            }
    ) {
        Text(
            text = settingButton.name,
            style = MaterialTheme.typography.button.copy(
                color = Color.Gray,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}