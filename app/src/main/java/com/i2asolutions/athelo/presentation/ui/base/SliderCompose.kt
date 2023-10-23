package com.i2asolutions.athelo.presentation.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.presentation.model.slider.SelectorStep
import com.i2asolutions.athelo.presentation.ui.theme.body1
import com.i2asolutions.athelo.presentation.ui.theme.darkPurple
import com.i2asolutions.athelo.presentation.ui.theme.gray
import com.i2asolutions.athelo.presentation.ui.theme.lightGray
import kotlin.math.max
import kotlin.math.roundToInt


@Composable
fun Selector(
    modifier: Modifier = Modifier,
    steps: Array<SelectorStep> = emptyArray(),
    onValueChange: (Int) -> Unit = {},
    maxValue: Float = 99f,
    enable: Boolean = true,
    selectedPosition: () -> Float,
) {
    val rememberSteps = remember { steps }
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            rememberSteps.forEachIndexed { index, selectorStep ->
                StepCell(
                    step = selectorStep,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = MutableInteractionSource(),
                        enabled = enable
                    ) {
                        onValueChange(max(1, (index * 100f / max(1, steps.size - 1)).toInt()))
                    })
                if (index < steps.size - 1)
                    Spacer(modifier = Modifier.weight(1f))
            }
        }
        Slider(
            value = selectedPosition(), onValueChange = {
                val selectedStep = it.roundToInt()
                onValueChange(selectedStep)
            },

            valueRange = 1f..maxValue,
            steps = max(0, steps.size - 2),
            colors = SliderDefaults.colors(
                thumbColor = darkPurple,
                disabledThumbColor = darkPurple,
                activeTrackColor = darkPurple,
                inactiveTrackColor = lightGray,
                activeTickColor = darkPurple,
                inactiveTickColor = lightGray,
                disabledActiveTickColor = darkPurple,
                disabledActiveTrackColor = darkPurple,
                disabledInactiveTickColor = lightGray,
                disabledInactiveTrackColor = lightGray
            ),
            enabled = enable,
            modifier = Modifier
                .padding(start = 6.dp, end = 2.dp)
                .fillMaxWidth()
                .height(10.dp)
        )
    }
}

@Composable
private fun RowScope.StepCell(step: SelectorStep, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        step.iconId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "",
                modifier = Modifier.size(32.dp)
            )
        }
        step.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.body1.copy(
                    color = gray,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .padding(top = 3.dp)
                    .sizeIn(minWidth = 32.dp)
            )
        }
        Icon(
            painter = painterResource(id = R.drawable.step_line),
            contentDescription = null,
            modifier = Modifier.padding(vertical = 3.dp),
            tint = gray
        )
    }
}

@Preview
@Composable
fun WellBeingSelectorPrev() {
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        var selectValue by remember { mutableStateOf(0f) }
        Selector(
            steps = arrayOf(
                SelectorStep(name = "1"),
                SelectorStep(name = "2"),
                SelectorStep(name = "3"),
                SelectorStep(name = "4"),
                SelectorStep(name = "5"),
                SelectorStep(name = "6"),
                SelectorStep(name = "7"),
            ),
            onValueChange = {
                selectValue = it.toFloat()
                debugPrint(it)
            }
        ) { selectValue }
    }
}

@Preview
@Composable
fun DisabledWellBeingSelectorPrev() {
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        var selectValue by remember { mutableStateOf(0f) }
        Selector(
            steps = arrayOf(
                SelectorStep(name = "1"),
                SelectorStep(name = "2"),
                SelectorStep(name = "3"),
                SelectorStep(name = "4"),
                SelectorStep(name = "5"),
                SelectorStep(name = "6"),
                SelectorStep(name = "7"),
            ),
            onValueChange = {
                selectValue = it.toFloat()
                debugPrint(it)
            },
            enable = false
        ) { selectValue }
    }
}