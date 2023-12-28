package com.athelohealth.mobile.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp,
        lineHeight = 30.sp,
        textAlign = TextAlign.Start,
        color = purple,
    ),
    displayMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 36.sp,
        textAlign = TextAlign.Center,
        color = black,
    ),
    displaySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        textAlign = TextAlign.Center,
        color = black,
    ),
    headlineSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        textAlign = TextAlign.Start,
        color = gray,
    ),
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 27.sp,
        textAlign = TextAlign.Left,
        color = gray,
    ),
    labelLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start,
        color = black,
    ),
    labelMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Start,
        color = black,
    ),
    bodyMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        textAlign = TextAlign.Start,
        color = black,
    ),
    titleSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Start,
        color = gray,
    ),
    labelSmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 27.sp,
        textAlign = TextAlign.Start,
        color = black,
    ),
    bodySmall = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 27.sp,
        textAlign = TextAlign.Left,
        color = darkPurple
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        textAlign = TextAlign.Left,
        color = black
    ),
    headlineLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 30.sp,
        textAlign = TextAlign.Left,
        color = black
    ),
    headlineMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        textAlign = TextAlign.Left,
        color = black
    ),
)

val Typography.headline30
    get() = displayLarge

val Typography.headline24
    get() = displayMedium

val Typography.headline20
    get() = displaySmall

val Typography.paragraph
    get() = bodyLarge
val Typography.subHeading
    get() = headlineSmall

val Typography.button
    get() = labelLarge
val Typography.textField
    get() = labelMedium
val Typography.body1
    get() = bodyMedium
val Typography.subtitle
    get() = titleSmall
val Typography.link
    get() = labelSmall
val Typography.normalText
    get() = bodySmall
val Typography.bold20
    get() = headlineLarge
val Typography.bold24
    get() = headlineMedium