package com.athelohealth.mobile.presentation.ui.share.appointment.joinAppointment

import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.athelohealth.mobile.extensions.debugPrint

@Composable
fun AppointmentRoom(
    viewModel: JoinAppointmentViewModel
) {

    val publisherView = viewModel.publisherView.value
    val subscriberView = viewModel.subscriberView.value
    var removeAllView = false

    LaunchedEffect(key1 = true) {
        removeAllView = viewModel.removeAllSubscriberView.value
    }

    debugPrint("Appointment room called...")

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                FrameLayout(context)
            },
            update = { view ->
                debugPrint("PublisherView added...")
                publisherView?.let { view.addView(it) }
            }
        )

        AndroidView(
            modifier = Modifier
                .width(90.dp)
                .height(120.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
                .background(Color.LightGray),
            factory = { context ->
                FrameLayout(context)
            },
            update = { view ->
                debugPrint("SubscriberView added...")
                subscriberView?.let {
                    if (removeAllView)
                        view.removeAllViews()
                    view.addView(it)
                }
            })
    }
}

