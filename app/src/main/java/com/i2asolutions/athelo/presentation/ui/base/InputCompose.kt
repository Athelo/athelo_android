@file:OptIn(ExperimentalFoundationApi::class)
@file:Suppress("UNUSED_PARAMETER")

package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.displayAsDifferentDateFormat
import com.i2asolutions.athelo.extensions.toBirthDate
import com.i2asolutions.athelo.presentation.model.CalendarMode
import com.i2asolutions.athelo.presentation.model.enums.EnumItem
import com.i2asolutions.athelo.presentation.model.timeDate.*
import com.i2asolutions.athelo.presentation.ui.theme.*
import com.i2asolutions.athelo.utils.consts.DATE_FORMAT_INPUT_DISPLAY_BIRTH_DATE
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun InputTextField(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    initialValue: String = "",
    labelText: String,
    readOnly: Boolean = false,
    imeAction: ImeAction,
    onChange: (TextFieldValue) -> Unit,
    keyboardActions: KeyboardActions,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    isPasswordField: Boolean = false,
    bringIntoViewRequester: BringIntoViewRequester? = null,
    textFormatter: (TextFieldValue) -> TextFieldValue = { it },
    disabledBackgroundColor: Color = gray,
    errorMessage: String = "Invalid value",
    isErrorShown: Boolean = false,
    hint: String,
    hideShadow: Boolean = false,
    singleLine: Boolean = false
) {
    val passwordVisibility = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val textState = remember(initialValue) {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }
    val focused = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier,
                textAlign = TextAlign.Start,
                text = labelText,
                color = if (isErrorShown) red else gray,
                style = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    fontFamily = fonts
                )
            )
        }
        Box(
            modifier = Modifier
                .shadow(
                    if (hideShadow) 0.dp else 3.dp,
                    CircleShape,
                )
                .background(
                    color = if (readOnly) disabledBackgroundColor else white,
                    shape = CircleShape
                )
                .padding(horizontal = 16.dp),
        ) {
            if (!focused.value && textState.value.text.isBlank()) {
                Text(
                    color = lightGray,
                    modifier = Modifier.align(CenterStart),
                    text = hint,
                    style = MaterialTheme.typography.textField.copy(fontSize = 16.sp)
                )
            }
            TextField(
                textStyle = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    color = if (isErrorShown) red else if (focused.value) darkPurple else if (textState.value.text.isNotBlank()) black else lightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        focused.value = focusState.isFocused
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(350)
                                bringIntoViewRequester?.bringIntoView()
                            }
                        }
                    },
                readOnly = readOnly,
                maxLines = 1,
                singleLine = singleLine,
                value = textState.value,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = if (isErrorShown) red else if (focused.value) purple else lightGray,
                    cursorColor = purple,
                    disabledTextColor = Color.Black,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    val textValue = textFormatter(it)
                    textState.value = textValue
                    onChange(textValue)
                },
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = keyboardType,
                    capitalization = capitalization
                ),
                visualTransformation = if (!isPasswordField) visualTransformation else {
                    if (passwordVisibility.value)
                        visualTransformation else
                        PasswordVisualTransformation('*')
                },
                trailingIcon = {
                    if (isPasswordField) {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                modifier = Modifier.size(15.dp),
                                painter = painterResource(
                                    if (!passwordVisibility.value)
                                        R.drawable.eye_hidden
                                    else
                                        R.drawable.eye
                                ),
                                contentDescription = null,
                                tint = if (passwordVisibility.value) darkPurple else gray
                            )
                        }
                    }
                },
            )
        }
    }
}

@Composable
fun SearchInputTextField(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    initialValue: String = "",
    readOnly: Boolean = false,
    imeAction: ImeAction = ImeAction.Search,
    onChange: (TextFieldValue) -> Unit,
    keyboardActions: KeyboardActions,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    isPasswordField: Boolean = false,
    bringIntoViewRequester: BringIntoViewRequester? = null,
    textFormatter: (TextFieldValue) -> TextFieldValue = { it },
    disabledBackgroundColor: Color = gray,
    hint: String,
    tailingIconClick: () -> Unit = {},
    hideShadow: Boolean = false
) {
    val coroutineScope = rememberCoroutineScope()

    val textState = remember(initialValue) {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }
    val focused = remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    if (hideShadow) 0.dp else 3.dp,
                    CircleShape,
                )
                .background(
                    color = if (readOnly) disabledBackgroundColor else white,
                    shape = CircleShape
                ),
        ) {
            if (!focused.value && textState.value.text.isBlank()) {
                Text(
                    color = lightGray,
                    modifier = Modifier
                        .align(CenterStart)
                        .padding(horizontal = 48.dp),
                    text = hint,
                    style = MaterialTheme.typography.textField.copy(fontSize = 16.sp)
                )
            }
            TextField(
                textStyle = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    color = if (focused.value) darkPurple else if (textState.value.text.isNotBlank()) black else lightGray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        focused.value = focusState.isFocused
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(350)
                                bringIntoViewRequester?.bringIntoView()
                            }
                        }
                    },
                readOnly = readOnly,
                maxLines = 1,
                value = textState.value,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = if (focused.value) purple else lightGray,
                    cursorColor = purple,
                    disabledTextColor = Color.Black,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    val textValue = textFormatter(it)
                    textState.value = textValue
                    onChange(textValue)
                },
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = keyboardType,
                    capitalization = capitalization
                ),
                visualTransformation = visualTransformation,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(24.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        tailingIconClick()
                    }) {
                        Icon(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(24.dp),
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = null,
                            tint = gray
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun DropDownMenuInput(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    label: Int? = R.string.What_best_describe_you,
    selectedItem: EnumItem = EnumItem.EMPTY,
    isErrorShown: Boolean = false,
    data: List<EnumItem>,
    onItemSelect: (EnumItem) -> Unit,
    readOnly: Boolean = false,
    displayItems: Int = -1,
) {
    var expanded by remember { mutableStateOf(false) }
    val maxHeight = remember { (displayItems * 56 + 16).dp }
    Column(modifier = modifier) {
        if (label != null)
            Text(
                modifier = Modifier
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Start,
                text = stringResource(id = label),
                color = if (isErrorShown) red else gray,
                style = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    fontFamily = fonts
                )
            )
        Row(
            modifier = Modifier
                .height(56.dp)
                .shadow(3.dp, CircleShape)
                .background(white, CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (!readOnly) {
                        val newValue = !expanded
                        expanded = newValue
                    }
                },
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = if (selectedItem == EnumItem.EMPTY) stringResource(id = R.string.Choose_Item) else selectedItem.label,
                style = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    color = if (selectedItem == EnumItem.EMPTY) lightGray else gray
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
            if (!readOnly)
                Image(
                    painter = painterResource(id = R.drawable.arrows),
                    contentDescription = "dropDowArrow",
                    modifier = Modifier
                        .rotate(if (expanded) 180f else 0f)
                        .padding(horizontal = 16.dp)

                )
        }
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .composed {
                if (displayItems > 0 && data.size > displayItems) {
                    Modifier.requiredHeightIn(max = maxHeight)
                } else
                    Modifier
            },
        properties = PopupProperties(focusable = true),
        offset = DpOffset(16.dp, 0.dp),
        showScrollBar = displayItems > 0 && data.size > displayItems,
        maxHeight = if (displayItems > 0 && data.size > displayItems) maxHeight else 0.dp
    ) {
        data.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    onItemSelect(item)
                    expanded = false
                }, modifier = Modifier
                    .height(56.dp)
                    .background(Color.Transparent)
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.textField.copy(fontSize = 16.sp),
                    modifier = Modifier.weight(1f)
                )
                if (selectedItem == item)
                    Image(
                        painter = painterResource(id = R.drawable.check),
                        contentDescription = "check Button"
                    )
            }
        }
    }

}

@Composable
fun ChatInput(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    bringIntoViewRequester: BringIntoViewRequester? = null,
    sendButtonClick: (String) -> Unit = {},
    hint: String = stringResource(id = R.string.Type_your_message_here),
) {
    val coroutineScope = rememberCoroutineScope()
    val focused = remember { mutableStateOf(false) }
    var textState by remember(initialValue) {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }
    Row(
        modifier = modifier
            .navigationBarsPadding()
            .imePadding()
            .background(background)
    ) {
        Box(modifier = modifier.weight(1f)) {
            if (!focused.value && textState.text.isBlank()) {
                Text(
                    color = lightGray,
                    modifier = Modifier
                        .align(CenterStart)
                        .padding(start = 16.dp),
                    text = hint,
                    style = MaterialTheme.typography.textField.copy(fontSize = 16.sp)
                )
            }
            TextField(
                textStyle = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    color = if (textState.text.isNotBlank()) black else lightGray
                ),
                modifier = Modifier
                    .heightIn(0.dp, 200.dp)
                    .onFocusEvent { focusState ->
                        focused.value = focusState.isFocused
                        if (focusState.isFocused) {
                            coroutineScope.launch {
                                delay(350)
                                bringIntoViewRequester?.bringIntoView()
                            }
                        }
                    },
                value = textState,
                onValueChange = {
                    textState = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = black,
                    cursorColor = purple,
                    disabledTextColor = Color.Black,
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardActions = keyboardActions,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = imeAction,
                    keyboardType = keyboardType,
                    capitalization = capitalization
                ),
            )
        }
        Image(
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
                .clickable {
                    if (textState.text.isNotBlank()) {
                        sendButtonClick(textState.text)
                        textState = textState.copy(text = "")
                    }
                },
            painter = painterResource(id = R.drawable.ic_chat_send),
            contentDescription = stringResource(
                id = R.string.app_name
            )
        )
    }
}

@Composable
fun DropDownCalendarInput(
    modifier: Modifier = Modifier.padding(horizontal = 16.dp),
    label: Int = R.string.Month_and_your_of_birth,
    isErrorShown: Boolean = false,
    initValue: String?,
    onItemSelect: (BirthDate) -> Unit,
    readOnly: Boolean = false,
    displayFormat: String = DATE_FORMAT_INPUT_DISPLAY_BIRTH_DATE,
    calendarMode: CalendarMode = CalendarMode.MONTH,
    maxYearAgo: Int = 150
) {
    val expanded = remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Start,
            text = stringResource(id = label),
            color = if (isErrorShown) red else gray,
            style = MaterialTheme.typography.textField.copy(
                fontSize = 16.sp,
                fontFamily = fonts
            )
        )
        Row(
            modifier = Modifier
                .height(56.dp)
                .shadow(3.dp, CircleShape)
                .background(white, CircleShape)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    if (!readOnly) {
                        expanded.value = !expanded.value
                    }
                },
            verticalAlignment = CenterVertically
        ) {
            Text(
                text = if (!initValue.isNullOrBlank())
                    initValue.displayAsDifferentDateFormat(displayFormat)
                else
                    stringResource(id = R.string.Enter_month_and_year_of_birth),
                style = MaterialTheme.typography.textField.copy(
                    fontSize = 16.sp,
                    color = if (initValue.isNullOrBlank()) lightGray else gray
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
            )
            if (!readOnly)
                Image(
                    painter = painterResource(id = if (expanded.value) R.drawable.ic_calendar_full else R.drawable.ic_calendar_out),
                    contentDescription = "calendar picker icon",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)

                )
        }
    }
    val currentBirthDate = initValue.toBirthDate() ?: BirthDate.Default
    val calendarModeState = remember { mutableStateOf(calendarMode) }
    val rotation = remember { mutableStateOf(0f) }

    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        properties = PopupProperties(focusable = true),
        offset = DpOffset(16.dp, 16.dp)
    ) {
        val currentYear = remember {
            Calendar.getInstance()[Calendar.YEAR]
        }
        val minYear = remember {
            Calendar.getInstance()[Calendar.YEAR] - maxYearAgo
        }
        val currentMonth = remember {
            Calendar.getInstance()[Calendar.MONTH] + 1
        }

        val currentYearRange = remember {
            mutableStateOf(createYearRange(currentBirthDate.year))
        }
        DropdownMenuItem(onClick = {
            calendarModeState.value =
                if (CalendarMode.MONTH == calendarModeState.value) CalendarMode.YEARS else CalendarMode.MONTH
            rotation.value = if (CalendarMode.MONTH == calendarModeState.value) 0f else 180f
        }) {
            Text(
                text = currentBirthDate.displayDate,
                style = MaterialTheme.typography.paragraph.copy(color = neutral100),
                modifier = Modifier
                    .wrapContentWidth()
                    .align(CenterVertically)
            )

            Image(
                painter = painterResource(id = R.drawable.arrows),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotation.value)
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(
                calendarModeState.value == CalendarMode.YEARS,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_prev),
                        colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (currentYearRange.value.first().yearValue > minYear) 1.0f else 0.5f)),
                        contentDescription = "Arrow Prev",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable {
                                if (currentYearRange.value.first().yearValue > minYear) {
                                    currentYearRange.value =
                                        createYearRangeUntil(currentYearRange.value.first())
                                }
                            },
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_next),
                        colorFilter = ColorFilter.tint(calendarArrow.copy(alpha = if (currentYearRange.value.last().yearValue < currentYear) 1.0f else 0.5f)),
                        contentDescription = "Arrow Next",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                            .clickable {
                                if (currentYearRange.value.last().yearValue < currentYear) {
                                    currentYearRange.value =
                                        createYearRange(currentYearRange.value.last())
                                }
                            },
                    )
                }
            }
        }
        Box {
            YearLayout(
                state = currentYearRange,
                calendarModeState = calendarModeState,
                currentBirthDate = currentBirthDate,
                minYear = minYear,
                currentYear = currentYear,
                initValue = initValue,
                onItemSelect = onItemSelect
            )

            MonthLayout(
                calendarModeState = calendarModeState,
                currentBirthDate = currentBirthDate,
                currentYear = currentYear,
                currentMonth = currentMonth,
                initValue = initValue,
                onItemSelect = onItemSelect,
                expanded = expanded
            )
        }
    }
}

@Composable
private fun MonthLayout(
    data: Array<Month> = Month.values(),
    calendarModeState: MutableState<CalendarMode>,
    currentBirthDate: BirthDate,
    currentYear: Int,
    currentMonth: Int,
    initValue: String?,
    onItemSelect: (date: BirthDate) -> Unit,
    expanded: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = calendarModeState.value == CalendarMode.MONTH,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column {
            for (i in data.indices step 4) {
                val leftMonthItem = data[i]
                val middleLeftMonthItem = data[i + 1]
                val middleRightMonthItem = data[i + 2]
                val rightMonthItem = data[i + 3]
                DropdownMenuItem(onClick = {}) {
                    MonthCell(
                        enabled = currentBirthDate.year.yearValue < currentYear || middleLeftMonthItem.value <= currentMonth,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.month == leftMonthItem,
                        month = leftMonthItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (currentBirthDate.year.yearValue <= currentYear || leftMonthItem.value <= currentMonth) {
                                    onItemSelect(currentBirthDate.apply {
                                        month = leftMonthItem
                                    })
                                    expanded.value = false
                                }
                            }
                    )
                    MonthCell(
                        enabled = currentBirthDate.year.yearValue < currentYear || middleLeftMonthItem.value <= currentMonth,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.month == middleLeftMonthItem,
                        month = middleLeftMonthItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (currentBirthDate.year.yearValue < currentYear || middleLeftMonthItem.value <= currentMonth) {
                                    onItemSelect(currentBirthDate.apply {
                                        month = middleLeftMonthItem
                                    })
                                    expanded.value = false
                                }
                            }
                    )
                    MonthCell(
                        enabled = currentBirthDate.year.yearValue < currentYear || middleRightMonthItem.value <= currentMonth,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.month == middleRightMonthItem,
                        month = middleRightMonthItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (currentBirthDate.year.yearValue < currentYear || middleRightMonthItem.value <= currentMonth) {
                                    onItemSelect(currentBirthDate.apply {
                                        month = middleRightMonthItem
                                    })
                                    expanded.value = false
                                }
                            }
                    )
                    MonthCell(
                        enabled = currentBirthDate.year.yearValue < currentYear || rightMonthItem.value <= currentMonth,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.month == rightMonthItem,
                        month = rightMonthItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (currentBirthDate.year.yearValue < currentYear || rightMonthItem.value <= currentMonth) {
                                    onItemSelect(currentBirthDate.apply {
                                        month = rightMonthItem
                                    })
                                    expanded.value = false
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun MonthCell(month: Month, modifier: Modifier, selected: Boolean, enabled: Boolean) {
    Box(modifier = modifier, contentAlignment = Center) {
        Text(
            text = month.shortName,
            style = MaterialTheme.typography.textField.copy(
                color = if (selected) white else gray.copy(
                    alpha = if (enabled) 1f else 0.5f
                )
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    if (selected) darkPurple else Color.Transparent,
                    CircleShape
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}


@Composable
private fun YearLayout(
    state: State<YearRange>,
    calendarModeState: MutableState<CalendarMode>,
    currentBirthDate: BirthDate,
    minYear: Int,
    currentYear: Int,
    initValue: String?,
    onItemSelect: (BirthDate) -> Unit,
) {
    AnimatedVisibility(
        visible = calendarModeState.value == CalendarMode.YEARS,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
    ) {
        val data = state.value

        Column {
            for (i in data.indices step 4) {
                val leftYearItem = data[i]
                val middleLeftYearItem = data[i + 1]
                val middleRightYearItem = data[i + 2]
                val rightYearItem = data[i + 3]
                DropdownMenuItem(onClick = {}) {
                    YearCell(
                        enabled = leftYearItem.yearValue in minYear..currentYear,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.year == leftYearItem,
                        year = leftYearItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (leftYearItem.yearValue in minYear..currentYear) {
                                    onItemSelect(currentBirthDate.apply {
                                        year = leftYearItem
                                    })
                                    calendarModeState.value = CalendarMode.MONTH
                                }
                            }
                    )
                    YearCell(
                        enabled = middleLeftYearItem.yearValue in minYear..currentYear,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.year == middleLeftYearItem,
                        year = middleLeftYearItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (middleLeftYearItem.yearValue in minYear..currentYear) {
                                    onItemSelect(currentBirthDate.apply {
                                        year = middleLeftYearItem
                                    })
                                    calendarModeState.value = CalendarMode.MONTH
                                }
                            }
                    )
                    YearCell(
                        enabled = middleRightYearItem.yearValue in minYear..currentYear,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.year == middleRightYearItem,
                        year = middleRightYearItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (middleRightYearItem.yearValue in minYear..currentYear) {
                                    onItemSelect(currentBirthDate.apply {
                                        year = middleRightYearItem
                                    })
                                    calendarModeState.value = CalendarMode.MONTH
                                }
                            }
                    )
                    YearCell(
                        enabled = rightYearItem.yearValue in minYear..currentYear,
                        selected = !initValue.isNullOrBlank() && currentBirthDate.year == rightYearItem,
                        year = rightYearItem,
                        modifier = Modifier
                            .weight(1f)
                            .align(CenterVertically)
                            .clip(CircleShape)
                            .clickable {
                                if (rightYearItem.yearValue in minYear..currentYear) {
                                    onItemSelect(currentBirthDate.apply {
                                        year = rightYearItem
                                    })
                                    calendarModeState.value = CalendarMode.MONTH
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun YearCell(year: Year, modifier: Modifier, selected: Boolean, enabled: Boolean) {
    Box(modifier = modifier, contentAlignment = Center) {
        Text(
            text = year.yearValue.toString(),
            style = MaterialTheme.typography.textField.copy(
                color = if (selected) white else gray.copy(
                    alpha = if (enabled) 1f else 0.5f
                )
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(
                    if (selected) darkPurple else Color.Transparent,
                    CircleShape
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewInputPassword() {
    ChatInput()
}