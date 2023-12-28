package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Updater
import androidx.compose.ui.Modifier
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.headline24

@Composable
fun UpcomingScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(background)
    ) {
        Text(text = "Upcoming", style = MaterialTheme.typography.headline24)
    }
}