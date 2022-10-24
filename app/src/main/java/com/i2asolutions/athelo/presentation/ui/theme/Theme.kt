@file:Suppress("DEPRECATION")

package com.i2asolutions.athelo.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

internal val LightColorScheme = lightColorScheme(
    primary = purple,
    secondary = purple,
    tertiary = darkPurple,
    onPrimary = darkPurple,
    background = background,
    onBackground = black,
    error = red,
    onError = red,
    outline = purple,
    surface = Color.Transparent,
    surfaceTint = Color.Transparent
)

@Composable
fun AtheloTheme(
    activity: Activity,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            activity.window.statusBarColor = LightColorScheme.primary.copy(alpha = 0.0f).toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = typography,
        content = content
    )
}