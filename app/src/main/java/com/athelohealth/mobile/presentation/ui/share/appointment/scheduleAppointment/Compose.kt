@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import android.util.Log
import android.widget.CalendarView
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.athelohealth.mobile.R
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.presentation.model.appointment.Provider
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.MainButton
import com.athelohealth.mobile.presentation.ui.base.Toolbar
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.dividerColor
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.lightOlivaceous
import com.athelohealth.mobile.presentation.ui.theme.purple
import com.athelohealth.mobile.presentation.ui.theme.typography
import com.athelohealth.mobile.presentation.ui.theme.white
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar


@Composable
fun ScheduleMyAppointment(viewModel: ScheduleAppointmentViewModel) {
    val viewState = viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.value.isLoading },
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        HeaderContent(viewModel = viewModel)
    }
}

data class AppointmentData(val name: String, val hobby: String)

@Composable
fun HeaderContent(
    viewModel: ScheduleAppointmentViewModel
) {

    val handleEvent = viewModel::handleEvent

    Column {
        Toolbar(
            screenName = stringResource(id = R.string.schedule_my_appointment),
            showBack = true,
            onBackClick = {
                handleEvent.invoke(ScheduleAppointmentEvent.OnBackButtonClicked)
            })

        ExpandableList(viewModel)
    }
}

@Composable
fun ExpandableList(
    viewModel: ScheduleAppointmentViewModel
) {

    val contentFullViewState = viewModel.contentfulViewState.collectAsState()

    val isExpandedMap = rememberSavableSnapshotStateMap {
        List(contentFullViewState.value.size) { index: Int -> index to false }
            .toMutableStateMap()
    }

    debugPrint("ExpandableList: ${contentFullViewState.value.size}")

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(all = 12.dp),
        content = {
            contentFullViewState.value.forEachIndexed { index, provider ->
                HeaderSection(
                    name = provider.displayName,
                    hobby = "Test",
                    providerAvatar = provider.photo,
                    isExpanded = isExpandedMap[index] ?: false,
                    viewModel = viewModel,
                    onHeaderClicked = {
                        isExpandedMap[index] = !(isExpandedMap[index] ?: false)
                    }
                )
            }
        }
    )
}

fun LazyListScope.HeaderSection(
    name: String?,
    hobby: String?,
    providerAvatar: String?,
    isExpanded: Boolean,
    viewModel: ScheduleAppointmentViewModel,
    onHeaderClicked: () -> Unit
) {
    item {
        val rotationState by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .padding(all = 10.dp),
            colors = CardDefaults.cardColors(background),
            elevation = CardDefaults.cardElevation(5.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                onHeaderClicked.invoke()
            },
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = providerAvatar,
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .aspectRatio(1f / 1f)
                        .weight(1f)
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(4f),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = name.orEmpty(),
                        style = typography.labelMedium.copy(
                            color = gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = hobby.orEmpty(),
                        style = typography.bodyMedium.copy(
                            color = gray,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
                IconButton(
                    onClick = {
                        onHeaderClicked.invoke()
                    },
                    modifier = Modifier
                        .alpha(ContentAlpha.medium)
                        .weight(0.5f)
                        .rotate(rotationState)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrows),
                        contentDescription = stringResource(id = R.string.app_name)
                    )
                }
            }
        }

        if (isExpanded) {
            SectionItemContent(
                viewModel = viewModel,
                onHeaderClicked = {
                onHeaderClicked.invoke()
            })
        }
    }

}

@Composable
fun SectionItemContent(
    viewModel: ScheduleAppointmentViewModel,
    onHeaderClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        var isCTAButtonEnabled by remember { mutableStateOf(true) }
        var isDateSelectionUiVisible by remember { mutableStateOf(true) }
        var selectedDate by remember { mutableStateOf("") }
        var selectedTime by remember { mutableStateOf("") }
        var textId = if (isDateSelectionUiVisible) R.string.apply_button else R.string.schedule_button

        Text(
            text = if (isDateSelectionUiVisible) "Choose date:" else "Free time:",
            color = gray,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 12.dp, top = 12.dp)
        )

        if (isDateSelectionUiVisible) {
            ChooseDate(onDateSelected = { date ->
                selectedDate = date
                if(isCTAButtonEnabled.not()) isCTAButtonEnabled = true
            })
        } else {
            ChooseTime(selectedDate, viewModel, onTimeSelected = { time ->
                selectedTime = time
                if(isCTAButtonEnabled.not()) isCTAButtonEnabled = true
            }, changeUi = {
                isDateSelectionUiVisible = !isDateSelectionUiVisible
                isCTAButtonEnabled = !isCTAButtonEnabled
            })
        }

        MainButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
            textId = textId,
            background = purple,
            enableButton = isCTAButtonEnabled,
            onClick = {

                if(isCTAButtonEnabled) {
                    isDateSelectionUiVisible = !isDateSelectionUiVisible
                    isCTAButtonEnabled = !isCTAButtonEnabled

                    if(textId == R.string.schedule_button) {
                        viewModel::handleEvent.invoke(ScheduleAppointmentEvent.OnAppointmentScheduled("Great! Your appointment has been scheduled successfully!"))
                        onHeaderClicked.invoke()
                        Log.d("TextIdCheck", "SectionItemContent: ")
                    } else {
                        // Get the time slot for the selected date from the API
                        viewModel.getProvidersAvailability("02/07/2024")
                    }
                }
            }
        )
    }
}

@Composable
fun ChooseDate(onDateSelected: (String) -> Unit) {

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val formatter = DateTimeFormatter.ofPattern("dd LLL, EEEE")

    val currrentDate = Calendar.getInstance()
    currrentDate.add(Calendar.DATE, 0)

    onDateSelected.invoke(selectedDate.format(formatter))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(all = 10.dp),
        colors = CardDefaults.cardColors(background),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        // Using AndroidView to wrap the CalendarView
        AndroidView(
            factory = { context ->
                CalendarView(context).apply {
                    // Set any additional properties or listeners if needed
                    setOnDateChangeListener { _, year, month, dayOfMonth ->
                        selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        val formattedDate: String = selectedDate.format(formatter)
                        Log.d("FormattedDate", "ChooseDate: $formattedDate")
                        onDateSelected.invoke(formattedDate)
                    }
                    minDate = currrentDate.timeInMillis
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Composable
fun ChooseTime(
    selectedDate: String,
    viewModel: ScheduleAppointmentViewModel,
    onTimeSelected: (String) -> Unit,
    changeUi: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(10.dp),
        colors = CardDefaults.cardColors(background),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        val timeSlots = viewModel.providersAvailability.collectAsState().value

        var selected by remember { mutableStateOf<Int?>(null) }

        var height = when  {
            timeSlots.size <= 3 -> 74.dp
            timeSlots.size <= 6 -> 126.dp
            timeSlots.size > 6 -> 186.dp
            else -> { 186.dp}
        }

        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            Text(
                text = selectedDate,
                color = lightOlivaceous,
                modifier = Modifier
                    .border(
                        width = 1.dp, color = lightOlivaceous, shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 3.dp)
                    .clickable {
                        changeUi.invoke()
                    },
                fontSize = 12.sp
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                color = dividerColor,
                thickness = 2.dp
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(timeSlots) { timeSlot ->
                    GridItem(
                        timeSlot = timeSlot,
                        isSelected = selected == timeSlots.indexOf(timeSlot),
                        onSelected = { selectedTime ->
                            selected = if (selected == timeSlots.indexOf(timeSlot)) null
                            else timeSlots.indexOf(timeSlot)

                            onTimeSelected.invoke(selectedTime)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun GridItem(
    timeSlot: String,
    isSelected: Boolean,
    onSelected: (String) -> Unit
) {
    if (isSelected) {
        Text(
            text = timeSlot,
            color = white,
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(color = lightOlivaceous)
                .padding(all = 12.dp)
                .clickable { onSelected(timeSlot) },
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    } else {
        Text(
            text = timeSlot,
            color = darkPurple,
            modifier = Modifier
                .border(
                    width = 1.dp, color = darkPurple, shape = RoundedCornerShape(24.dp)
                )
                .padding(all = 12.dp)
                .clickable { onSelected(timeSlot) },
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Preview
@Composable
fun ContentPreview() {
    val viewModel: ScheduleAppointmentViewModel = viewModel()
//    ScheduleMyAppointment(viewModel)
//    HeaderSectionContent(name = "Ave Calvar", hobby = "Care Navigator", isExpanded = false) {}

    ChooseTime("17 Jan, Thursday", viewModel, {}) {}
//    ChooseDate {}
//    SectionItemContent()
}

fun <K, V> snapshotStateMapSaver() = Saver<SnapshotStateMap<K, V>, Any>(
    save = { state -> state.toList() },
    restore = { value ->
        @Suppress("UNCHECKED_CAST")
        (value as? List<Pair<K, V>>)?.toMutableStateMap() ?: mutableStateMapOf<K, V>()
    }
)

@Composable
fun <K, V> rememberSavableSnapshotStateMap(init: () -> SnapshotStateMap<K, V>): SnapshotStateMap<K, V> =
    rememberSaveable(saver = snapshotStateMapSaver(), init = init)