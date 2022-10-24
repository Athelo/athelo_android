package com.i2asolutions.athelo.extensions

import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonDefaults.emptyElevation() = elevatedButtonElevation(
    defaultElevation = 0.dp,
    pressedElevation = 0.dp,
    focusedElevation = 0.dp,
    hoveredElevation = 0.dp,
    disabledElevation = 0.dp,
)

@Composable
fun ButtonDefaults.emptyColors() = buttonColors(
    containerColor = Color.Transparent,
    contentColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = Color.Transparent,
)