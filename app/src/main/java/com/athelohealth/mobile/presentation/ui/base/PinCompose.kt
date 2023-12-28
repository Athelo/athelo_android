@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athelohealth.mobile.presentation.ui.theme.*


@Composable
fun PinEditText(
    modifier: Modifier = Modifier,
    maxCount: Int = 4,
    hideValue: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    enteredPin: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (!it.hasFocus) {
                        focusManager.clearFocus()
                    }
                },
            value = text,
            onValueChange = {
                text = it.filter { it.isDigit() }.take(maxCount)
                enteredPin(it.filter { it.isDigit() }.take(maxCount))
            },
            cursorBrush = SolidColor(Color.Transparent),
            textStyle = MaterialTheme.typography.textField.copy(
                color = Color.Transparent,
                lineHeight = 18.sp,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = keyboardOptions,
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            })
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = MutableInteractionSource()
                ) {
                    focusManager.clearFocus()
                },
        ) {
            for (i in 0 until maxCount) {
                PinItem(
                    modifier = Modifier
                        .size(42.dp)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            focusManager.clearFocus()
                            focusRequester.requestFocus()
                        },
                    hideValue = hideValue,
                    text = text.getOrElse(i) { ' ' }.toString()
                )
            }
        }
    }

}

@Composable
private fun PinItem(
    modifier: Modifier,
    text: String,
    hideValue: Boolean,
    shape: Shape = RoundedCornerShape(16.dp),
    style: TextStyle = MaterialTheme.typography.textField.copy(
        color = black,
        lineHeight = 18.sp,
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    ),
    emptyStyle: TextStyle = MaterialTheme.typography.textField.copy(
        color = lightGray,
        lineHeight = 18.sp,
        fontSize = 18.sp,
        textAlign = TextAlign.Center
    ),
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .then(modifier),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = white),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val isEmpty = text.isBlank()
            Text(
                text = if (isEmpty) "X" else if (hideValue) "*" else text,
                modifier = Modifier,
                style = if (isEmpty) emptyStyle else style,
                textAlign = TextAlign.Center,
            )
        }
    }
}