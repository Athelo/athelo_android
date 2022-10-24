package com.i2asolutions.athelo.presentation.ui.base.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.presentation.ui.theme.background
import com.i2asolutions.athelo.presentation.ui.theme.lightOlivaceous

@Composable
fun SimpleBarChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    barColor: Color = lightOlivaceous,
    bgColor: Color = Color(0xFFD8EEB5)
) {
    var shouldRecalculate = remember(data) { true }
    var cords: List<Float> = remember { listOf() }
    val measurements: SimpleBarChartMeasurement = remember { SimpleBarChartMeasurement() }
    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (shouldRecalculate) {
                shouldRecalculate = false
                measurements.setupDefault(this)
                cords = prepareDataSet(data, size, measurements)
            }

            var offset = 0
            for (i in 0 until 7) {
                //draw bg
                runCatching {
                    val bgLeft = cords[offset + 0]
                    val bgTop = cords[offset + 1]
                    val bgRight = cords[offset + 2]
                    val bgBottom = cords[offset + 3]
                    drawRoundRect(
                        color = bgColor,
                        topLeft = Offset(bgLeft, bgTop),
                        size = Size(bgRight - bgLeft, bgBottom - bgTop),
                        cornerRadius = CornerRadius(measurements.barRadius),
                    )

                    //draw bar
                    val left = cords[offset + 4]
                    val top = cords[offset + 5]
                    val right = cords[offset + 6]
                    val bottom = cords[offset + 7]
                    if (bottom - top > 0)
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(left, top),
                            size = Size(right - left, bottom - top),
                            cornerRadius = CornerRadius(measurements.barRadius),
                        )
                    offset += 8
                }
            }
        }
    }
}

private fun prepareDataSet(
    data: List<Float>,
    size: Size,
    measurements: SimpleBarChartMeasurement
): List<Float> {
    val cords = arrayListOf<Float>()
    data.forEachIndexed { index, value ->
        val left = index * measurements.dayColumnWidth
        //bg
        cords.add(left)
        cords.add(0f)
        cords.add(left + measurements.dayColumnWidth * measurements.barWidthRatioPercent)
        cords.add(size.height)
        //bar
        cords.add(left)
        cords.add(size.height - size.height * value)
        cords.add(left + measurements.dayColumnWidth * measurements.barWidthRatioPercent)
        cords.add(size.height)
    }
    return cords
}

class SimpleBarChartMeasurement(
    var barRadius: Float = 0f,
    var barWidthRatioPercent: Float = 0f,
    var dayColumnWidth: Float = 0f,
) {
    fun setupDefault(drawScope: DrawScope) = with(drawScope) {
        barWidthRatioPercent = 0.6f
        dayColumnWidth = size.width / (6 + barWidthRatioPercent)
        barRadius = dayColumnWidth * barWidthRatioPercent / 2
    }
}

@Preview
@Composable
fun PreviewSimpleBarChartChart() {
    SimpleBarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(background),
        data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f),
        barColor = lightOlivaceous,
        bgColor = Color(0xFFD8EEB5)
    )
}