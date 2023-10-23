package com.i2asolutions.athelo.presentation.ui.patient.wellbeing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.normalizeValue
import com.i2asolutions.athelo.presentation.model.base.InputType
import com.i2asolutions.athelo.presentation.model.calendar.toDay
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.health.Symptom
import com.i2asolutions.athelo.presentation.model.slider.SelectorStep
import com.i2asolutions.athelo.presentation.ui.base.*
import com.i2asolutions.athelo.presentation.ui.base.calendar.SimpleDayPicker
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.createMockSymptoms
import java.util.*

@Composable
fun WellbeingScreen(viewModel: WellbeingViewModel) {
    val state by viewModel.state.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { state.isLoading },
        modifier = Modifier.navigationBarsPadding()
    ) {
        Content(state = state, handleEvent = viewModel::handleEvent)
        state.removeConfirmPopup?.let {
            DeletePopup(
                onConfirmClick = {
                    viewModel.handleEvent(
                        WellbeingEvent.SymptomRemovedConfirmClick(
                            it
                        )
                    )
                },
                onCancelClick = { viewModel.handleEvent(WellbeingEvent.SymptomRemovedCancelClick) },
                title = stringResource(id = R.string.Remove_Symptom_confirmation_title),
                description = stringResource(id = R.string.Remove_Symptom_confirmation_message),
            )
        }
    }
}

@Composable
private fun Content(state: WellbeingViewState, handleEvent: (WellbeingEvent) -> Unit) {
    Column {
        Toolbar(
            screenName = stringResource(id = R.string.Track_My_Wellbeing),
            showBack = true,
            onBackClick = {
                handleEvent(WellbeingEvent.BackButtonClick)
            })
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            var editModeOn by remember { mutableStateOf(false) }
            SimpleDayPicker(
                modifier = Modifier.fillMaxWidth(),
                initDay = state.initialDay,
                selectedDay = state.selectedDay,
                onDayClick = {
                    editModeOn = false
                    handleEvent(WellbeingEvent.DayValueChanged(it))
                },
                minDay = Calendar.getInstance()
                    .also { calendar -> calendar.add(Calendar.DAY_OF_YEAR, -6) }.toDay(),
                maxDay = Calendar.getInstance().toDay(),
                showArrows = false
            )
            FeelingCell(handleEvent, state)
            SymptomsListCell(
                editModeOn = { editModeOn },
                symptoms = state.currentSymptoms,
                handleEvent = handleEvent
            )
            if (editModeOn) {
                AddSymptomCell(
                    handleEvent = handleEvent,
                    selectedItem = state.selectedSymptom,
                    options = state.allSymptoms
                )
            }
            if (!editModeOn)
                MainButton(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp)
                        .fillMaxWidth(),
                    textId = R.string.Edit_Information,
                    onClick = {
                        editModeOn = true
                    })
        }
    }
}

@Composable
private fun FeelingCell(
    handleEvent: (WellbeingEvent) -> Unit,
    state: WellbeingViewState
) {
    Text(
        text = stringResource(id = R.string.How_are_you_feeling_7_best),
        style = MaterialTheme.typography.headline20.copy(color = gray),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 24.dp)
    )
    Selector(
        modifier = Modifier.padding(horizontal = 16.dp),
        steps = arrayOf(
            SelectorStep(name = "1"),
            SelectorStep(name = "2"),
            SelectorStep(name = "3"),
            SelectorStep(name = "4"),
            SelectorStep(name = "5"),
            SelectorStep(name = "6"),
            SelectorStep(name = "7"),
        ),
        onValueChange = {
            handleEvent(WellbeingEvent.FeelingValueChanged(it))
        }
    ) { state.currentFeeling.toFloat() }
    MainButton(
        textId = R.string.Save_My_Feelings,
        onClick = { handleEvent(WellbeingEvent.SaveMyFeelingClick) },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 37.dp, bottom = 16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun SymptomsListCell(
    editModeOn: () -> Boolean,
    symptoms: List<Symptom>,
    handleEvent: (WellbeingEvent) -> Unit
) {
    Text(
        text = stringResource(id = R.string.What_symptom_do_you_have),
        style = MaterialTheme.typography.headline20.copy(
            gray, textAlign = TextAlign.Start
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
    )
    if (symptoms.isNotEmpty()) {
        FlowRow(
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth(),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 8.dp
        ) {
            symptoms.forEach { symptom ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .clip(CircleShape)
                        .shadow(3.dp, shape = CircleShape)
                        .background(white, CircleShape)
                        .wrapContentWidth()
                        .clickable {
                            handleEvent(WellbeingEvent.SymptomClick(symptom))
                        },
                    verticalAlignment = CenterVertically
                ) {
                    Text(
                        text = symptom.name,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .padding(start = 16.dp, end = if (editModeOn()) 8.dp else 16.dp)
                            .weight(1f, false)
                            .heightIn(min = 20.dp),
                        style = MaterialTheme.typography.button.copy(
                            color = darkPurple,
                            textAlign = TextAlign.Start
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (editModeOn()) {
                        IconButton(
                            onClick = {
                                handleEvent(WellbeingEvent.SymptomRemovedClick(symptom.id))
                            }, modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fi_sr_cross_small),
                                contentDescription = "Remove Symptom",
                                modifier = Modifier,
                                tint = gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddSymptomCell(
    selectedItem: EnumItem,
    options: List<EnumItem>,
    handleEvent: (WellbeingEvent) -> Unit
) {
    Text(
        text = stringResource(id = R.string.Your_symptom),
        style = MaterialTheme.typography.textField.copy(
            gray, textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    )
    Column(modifier = Modifier.padding(bottom = 24.dp)) {
        DropDownMenuInput(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            label = null,
            selectedItem,
            data = options,
            onItemSelect = {
                handleEvent(WellbeingEvent.InputValueChanged(InputType.DropDown(it.id)))
            },
            displayItems = 3
        )
    }
    SecondaryButton(
        text = R.string.plus_Add_Symptom,
        onClick = {
            handleEvent(WellbeingEvent.AddSymptomClick)
        },
        background = background,
        border = darkPurple,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
    )
}

@Preview
@Composable
fun ContentPrev() {
    Box(modifier = Modifier.background(background)) {
        var state by remember {
            mutableStateOf(
                WellbeingViewState(
                    initialFeeling = 10.normalizeValue(),
                    currentFeeling = 10.normalizeValue(),
                    currentSymptoms = createMockSymptoms(10)
                )
            )
        }
        Content(state = state) {
            state = state.copy(
                feelingButtonEnabled = !state.feelingButtonEnabled,
                initialFeeling = 10.normalizeValue()
            )
        }
    }
}
