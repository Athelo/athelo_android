@file:OptIn(ExperimentalTextApi::class)
@file:Suppress("DEPRECATION")

package com.athelohealth.mobile.presentation.ui.base

import android.os.Parcelable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.athelohealth.mobile.presentation.ui.theme.*
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioButton(
    val text: String,
    val includePadding: Boolean = false,
    val onClick: () -> Unit = {}
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RadioButton) return false

        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        return text.hashCode()
    }
}

@Composable
fun RowScope.RadioButton(
    onClick: () -> Unit,
    selectionProvider: () -> Boolean,
    modifier: Modifier,
    includePadding: Boolean = false,
    text: String
) {
    Column(
        modifier = modifier
            .padding(5.dp)
            .clip(CircleShape)
            .shadow(
                if (selectionProvider()) 2.dp else 0.dp,
                ambientColor = darkGray.copy(alpha = 0.2f)
            )
            .fillMaxHeight()
            .background(if (selectionProvider()) background else Color.Transparent)
            .weight(1f)
            .align(CenterVertically)
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = text,
            softWrap = true,
            style = MaterialTheme.typography.button.copy(
                color = gray,
                textAlign = TextAlign.Center,
                platformStyle = PlatformTextStyle(includeFontPadding = includePadding)
            ),
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .align(CenterHorizontally)
                .background(Color.Transparent),
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun RadioButtonGroup(
    selectionProvider: () -> Int = { 0 },
    modifier: Modifier,
    height: Dp = 54.dp,
    vararg buttons: RadioButton
) {
    val selected =
        remember { mutableStateOf(buttons[selectionProvider()]) }
    Row(
        modifier = modifier
            .height(height)
            .shadow(0.dp, shape = CircleShape, ambientColor = lightGray.copy(alpha = 0.2f))
            .clip(CircleShape)
            .background(lightGray.copy(alpha = 0.3f)),
    ) {
        buttons.map { button ->
            RadioButton(
                onClick = {
                    selected.value = button
                    button.onClick()
                },
                selectionProvider = { button == selected.value },
                modifier = Modifier,
                text = button.text,
                includePadding = button.includePadding
            )
        }
    }
}

@Preview
@Composable
fun RadioButtonPrev() {
    Row(modifier = Modifier.height(50.dp)) {
        RadioButton(onClick = { }, modifier = Modifier, text = "Test", selectionProvider = { true })
        RadioButton(
            onClick = { },
            modifier = Modifier,
            text = "Test Long text TO check how it would look like",
            selectionProvider = { false })
    }
}

@Preview
@Composable
fun RadioButtonGroupPrev() {
    RadioButtonGroup(
        modifier = Modifier.padding(horizontal = 16.dp),
        buttons = arrayOf(
            RadioButton(text = "Test 1"),
            RadioButton(text = "Test 2"),
            RadioButton(text = "Test Long text TO check how it would look like")
        ),
    )
}