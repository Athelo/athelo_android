@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.athelohealth.mobile.presentation.ui.share.appointment.scheduleAppointment

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.ui.base.BoxScreen
import com.athelohealth.mobile.presentation.ui.base.Toolbar
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.black
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.subHeading

@Composable
fun ScheduleMyAppointment(viewModel: ScheduleAppointmentViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    BoxScreen(
        viewModel = viewModel,
        showProgressProvider = { viewState.isLoading },
        modifier = Modifier
            .navigationBarsPadding()
    ) {
        OuterContent(handleEvent = viewModel::handleEvent)
    }
}

data class AppointmentData(val name: String, val hobby: String)

@Composable
fun OuterContent(handleEvent: (ScheduleAppointmentEvent) -> Unit) {
    val dataList = listOf(
        AppointmentData("Pankaj", "Abcde"),
        AppointmentData("Pradip", "Abcde"),
        AppointmentData("Rajesh", "Abcde"),
        AppointmentData("Vandit", "Abcde"),
        AppointmentData("Vishal", "Abcde")
    )

    Column {
        Toolbar(
            screenName = stringResource(id = R.string.schedule_my_appointment),
            showBack = true,
            onBackClick = {
                handleEvent.invoke(ScheduleAppointmentEvent.OnBackButtonClicked)
            })

        LazyColumn(modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(all = 12.dp)
        ) {
            items(dataList) { item ->
                HeaderContent(
                    name = item.name,
                    hobby = item.hobby,
                    onHeaderClicked = { }
                )
            }
        }
    }
}

@Composable
fun HeaderContent(
    name: String,
    hobby: String,
    onHeaderClicked: () -> Unit
) {

    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .padding(all = 12.dp),
        colors = CardDefaults.cardColors(background),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            onHeaderClicked.invoke()
            expandedState = !expandedState
        },
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_user_avatar),
                contentDescription = stringResource(id = R.string.app_name),
                modifier = Modifier.size(70.dp).weight(1f)
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(4f),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.subHeading.copy(
                        color = black,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = hobby,
                    style = MaterialTheme.typography.subHeading.copy(
                        color = gray,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
            IconButton(
                onClick = {
                    expandedState = !expandedState
                    onHeaderClicked.invoke()
                },
                modifier = Modifier
                    .alpha(ContentAlpha.medium)
                    .weight(1f)
                    .rotate(rotationState)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.app_name)
                )
            }
        }
    }

//    if (expandedState) {
//        ChooseDate()
//    }
}

@Composable
fun ChooseDate() {

}

@Composable
fun ChooseTime() {

}

@Preview
@Composable
fun ContentPreview() {
//    val viewModel: ScheduleAppointmentViewModel = viewModel()
//    ScheduleMyAppointment(viewModel)

    HeaderContent(name = "Ave Calvar", hobby = "Care Navigator") {

    }
}