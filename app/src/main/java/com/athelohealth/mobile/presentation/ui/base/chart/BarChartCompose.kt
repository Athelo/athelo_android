@file:OptIn(ExperimentalMaterial3Api::class)

package com.athelohealth.mobile.presentation.ui.base.chart

import android.content.Context
import android.graphics.Canvas
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.athelohealth.mobile.R
import com.athelohealth.mobile.presentation.model.chart.BarChartDataSet
import com.athelohealth.mobile.presentation.model.chart.BarChartEntry
import com.athelohealth.mobile.presentation.model.chart.ChartEntry
import com.athelohealth.mobile.presentation.model.heartRate.HeartRateBarChartEntry
import com.athelohealth.mobile.presentation.ui.theme.*
import com.athelohealth.mobile.utils.createMockWeeklySleepDetails
import kotlin.math.ceil

const val DEF_BAR_CHART_Y_AXIS_MAX = 8f

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    chartData: BarChartDataSet,
    yAxisLabelWidth: Dp = 30.dp,
    popupContent: @Composable BoxScope.(BarChartEntry) -> Unit
) {
    val context = LocalContext.current

    val yAxisLabelWidthPx = with(LocalDensity.current) { yAxisLabelWidth.toPx() }
    var shouldRecalculate = remember(chartData) { true }
    var cords: List<Float> = remember { listOf() }
    val measurements: BarChartMeasurement =
        remember { BarChartMeasurement(yAxisLabelWidth = yAxisLabelWidthPx) }
    var clickedEntry: BarChartEntry? by remember(chartData) { mutableStateOf(null) }
    var cloudSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .pointerInput(chartData) {
                detectTapGestures(
                    onTap = { clickedPoint ->
                        clickedEntry =
                            chartData.values.firstOrNull { it.clickRect.contains(clickedPoint) }
                    }
                )
            }) {
            if (shouldRecalculate) {
                shouldRecalculate = false
                measurements.setupDefault(yAxisLabelWidth, this, chartData)
                cords = prepareDataSet(
                    chartData,
                    measurements.graphHeight,
                    measurements.yAxisLabelWidth + measurements.axisLineWidth,
                    measurements.hourRowHeight,
                    measurements.dayColumnWidth,
                    measurements.dayColumnWidth * measurements.barWidthRatioPercent,
                    measurements.barStrokeWidth,
                    measurements.barRadius
                )
            }

            //yAxis line
            drawLine(
                color = lightGray,
                start = Offset(measurements.yAxisLineCenterY, 0f),
                end = Offset(measurements.yAxisLineCenterY, measurements.graphHeight),
                strokeWidth = measurements.axisLineWidth,
                alpha = 0.8f
            )

            //xAxis line
            drawLine(
                color = lightGray,
                start = Offset(measurements.yAxisLabelWidth, measurements.xAxisLineCenterX),
                end = Offset(size.width, measurements.xAxisLineCenterX),
                strokeWidth = measurements.axisLineWidth,
                alpha = 0.8f
            )

            if (chartData.xLabelStep == 1) {
                chartData.values.forEachIndexed { i, barChartEntry ->
                    //xAxis dotted line
                    val x =
                        measurements.yAxisLabelWidth + measurements.axisLineWidth + measurements.dayColumnWidth * i + measurements.dayColumnWidth / 2
                    if (chartData.drawYLines)
                        drawLine(
                            lightGray,
                            Offset(x, 0f),
                            Offset(x, measurements.graphHeight),
                            strokeWidth = measurements.dayLineWidth,
                            pathEffect = PathEffect.dashPathEffect(intervals = measurements.dayLineDashPattern),
                            alpha = 0.8f
                        )

                    //xAxis label
                    drawIntoCanvas {
                        drawNativeText(
                            context = context,
                            canvas = it.nativeCanvas,
                            position = Offset(x, measurements.xAxisLabelCenterY),
                            text = chartData.xAxisFormatter(barChartEntry),
                            font = R.font.inter_semi_bold,
                            size = 10.sp.toPx(),
                            textColor = gray.toArgb()
                        )
                    }
                }
            } else {
                for (i in chartData.xAxisMinLabel..chartData.xAxisMaxLabel) {
                    if (!chartData.hourMode && i != 0 && (i + 1) % chartData.xLabelStep != 0) continue
                    else if (chartData.hourMode && (i % chartData.xLabelStep != 0 || i == chartData.xAxisMaxLabel)) continue
                    val x =
                        measurements.yAxisLabelWidth + measurements.axisLineWidth + measurements.dayColumnWidth * i + measurements.dayColumnWidth / 2
                    drawLine(
                        lightGray,
                        Offset(x, 0f),
                        Offset(x, measurements.graphHeight),
                        strokeWidth = measurements.dayLineWidth,
                        pathEffect = PathEffect.dashPathEffect(intervals = measurements.dayLineDashPattern),
                        alpha = 0.3f
                    )

                    //xAxis label
                    if (i < chartData.values.size)
                        drawIntoCanvas {
                            drawNativeText(
                                context = context,
                                canvas = it.nativeCanvas,
                                position = Offset(x, measurements.xAxisLabelCenterY),
                                text = chartData.xAxisFormatter(chartData.values[i]),
                                font = R.font.inter_semi_bold,
                                size = 10.sp.toPx(),
                                textColor = gray.toArgb()
                            )
                        }
                }
            }

            for (i in 0..measurements.maxValue) {
                //yAxis dotted line
                val y = measurements.graphHeight - measurements.hourRowHeight * i
                if (i % chartData.yLabelStep == 0) {
                    if (i != 0)
                        drawLine(
                            lightGray,
                            Offset(measurements.yAxisLabelWidth + measurements.axisLineWidth, y),
                            Offset(
                                measurements.yAxisLabelWidth + measurements.axisLineWidth + measurements.graphWidth,
                                y
                            ),
                            strokeWidth = measurements.dayLineWidth,
                            pathEffect = PathEffect.dashPathEffect(intervals = measurements.dayLineDashPattern),
                            alpha = 0.3f
                        )

                    //yAxis label
                    val x = measurements.yAxisLabelWidth / 2
                    drawIntoCanvas {
                        drawNativeText(
                            context = context,
                            canvas = it.nativeCanvas,
                            position = Offset(x, y),
                            text = chartData.yAxisFormatter(i.toFloat()),
                            font = R.font.inter_semi_bold,
                            size = 10.sp.toPx(),
                            textColor = gray.toArgb()
                        )
                    }
                }
            }

            var offset = 0
            if (chartData.values.any { it is HeartRateBarChartEntry }) {
                for (i in cords.indices step 4) {
                    val left = cords[i + 0]
                    val top = cords[i + 1]
                    val right = cords[i + 2]
                    val bottom = cords[i + 3]
                    drawRoundRect(
                        color = lightOlivaceous,
                        topLeft = Offset(left, top),
                        size = Size(right - left, bottom - top),
                        cornerRadius = CornerRadius(measurements.barRadius)
                    )
                }
            } else {
                for (entry in chartData.values) {
                    if (entry.values.sumOf { it.value.toDouble() } == 0.0) continue
                    //draw gradient
                    var left = cords[offset + 0]
                    var top = cords[offset + 1]
                    var right = cords[offset + 2]
                    var bottom = cords[offset + 3]
                    offset += 4
                    drawRoundedRect(
                        left,
                        top,
                        right,
                        bottom,
                        measurements.barRadius,
                        entry.strokeGradient
                    )
                    //draw bars
                    entry.values.forEachIndexed { index, chartEntry ->
                        left = cords[offset + 0]
                        top = cords[offset + 1]
                        right = cords[offset + 2]
                        bottom = cords[offset + 3]
                        offset += 4
                        if (index == 0)
                            drawRoundedRect(
                                left,
                                top,
                                right,
                                bottom,
                                measurements.innerBarRadius,
                                chartEntry.bgColor
                            )
                        else
                            drawRect(left, top, right, bottom, chartEntry.bgColor)

                        //draw text on bar
                        drawIntoCanvas {
                            drawNativeText(
                                context,
                                it.nativeCanvas,
                                Offset(left + (right - left) / 2, top + (bottom - top) / 2),
                                chartData.valueFormatter(chartEntry),
                                measurements.valueFont,
                                measurements.valueTextSize,
                                chartEntry.textColor
                            )
                        }
                    }
                }
            }
        }


        clickedEntry?.let { ce ->
            val x = maxOf(
                0f,
                minOf(
                    measurements.viewWidth - cloudSize.width,
                    ce.clickRect.center.x - cloudSize.width / 2
                )
            )
            val y = maxOf(
                0f,
                ce.clickRect.top - measurements.valueCloudHeight - measurements.valueCloudBottomPadding
            )
            val xDp = LocalDensity.current.run { x.toDp() }
            val yDp = LocalDensity.current.run { y.toDp() }
            val heightDp = LocalDensity.current.run { measurements.valueCloudHeight.toDp() }
            Card(
                modifier = Modifier
                    .height(heightDp)
                    .widthIn(min = 100.dp)
                    .offset(x = xDp, y = yDp)
                    .clip(RoundedCornerShape(12.dp))
                    .onSizeChanged {
                        cloudSize = it
                    },
                colors = CardDefaults.cardColors(white),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(modifier = Modifier.fillMaxHeight()) {
                    popupContent(ce)
                }
            }
        }
    }
}

private fun DrawScope.drawRect(l: Float, t: Float, r: Float, b: Float, color: Int) {
    drawRoundedRect(l, t, r, b, 0f, listOf(color, color))
}

private fun DrawScope.drawRoundedRect(
    l: Float,
    t: Float,
    r: Float,
    b: Float,
    radius: Float,
    color: Int
) {
    drawRoundedRect(l, t, r, b, radius, listOf(color, color))
}

private fun DrawScope.drawRoundedRect(
    l: Float,
    t: Float,
    r: Float,
    b: Float,
    radius: Float,
    gradient: List<Int>
) {
    val path = Path()
    path.reset()
    path.moveTo(r, t + radius)
    path.relativeQuadraticBezierTo(0f, -radius, -radius, -radius)
    path.relativeLineTo(-((r - l) - (2 * radius)), 0f)
    path.relativeQuadraticBezierTo(-radius, 0f, -radius, radius)
    path.relativeLineTo(0f, ((b - t) - (radius)))
    path.relativeLineTo((r - l), 0f)
    path.relativeLineTo(0f, -((b - t) - (2 * radius)))
    path.close()
    drawPath(path, Brush.verticalGradient(gradient.map { Color(it) }))
}

private fun drawNativeText(
    context: Context,
    canvas: Canvas,
    position: Offset,
    text: String,
    @FontRes font: Int,
    size: Float,
    @ColorInt textColor: Int
) {
    val textPaint = TextPaint().apply {
        typeface = ResourcesCompat.getFont(context, font)
        textSize = size
        color = textColor
    }
    val textRect = android.graphics.Rect()
    textPaint.getTextBounds(text, 0, text.length, textRect)

    canvas.drawText(
        text,
        position.x - (textRect.right - textRect.left) / 2,
        position.y + (textRect.bottom - textRect.top) / 2,
        textPaint
    )
}

private fun prepareDataSet(
    dataSet: BarChartDataSet,
    graphHeight: Float,
    graphLeft: Float,
    unitPixels: Float,
    placeForSingleBar: Float,
    barWidth: Float,
    barStrokeWidth: Float,
    barRadius: Float,
): List<Float> {
    val cords = arrayListOf<Float>()
    dataSet.values.forEachIndexed { index, barChartEntry ->
        val centerX = placeForSingleBar * index + placeForSingleBar / 2
        if (barChartEntry is HeartRateBarChartEntry) {
            var clickRect = androidx.compose.ui.geometry.Rect(
                graphLeft + centerX - barWidth / 2,
                graphHeight - (barChartEntry.values.maxOfOrNull { it.value } ?: 0f) * unitPixels,
                graphLeft + centerX + barWidth / 2,
                graphHeight
            )
            val splitValues = barChartEntry.values.split(unitPixels, barWidth * 3)
            var isTopCircle = false
            splitValues.forEach {
                var top = graphHeight - it.second * unitPixels
                var bottom = graphHeight - it.first * unitPixels
                if (bottom - top < 2 * barRadius) {
                    val center = top + (bottom - top) / 2
                    top = center - barRadius
                    bottom = center + barWidth
                    isTopCircle = true
                } else {
                    isTopCircle = false
                }
                cords.add(clickRect.left)
                cords.add(top)
                cords.add(clickRect.right)
                cords.add(bottom)
            }
            if (isTopCircle) clickRect = clickRect.copy(top = clickRect.top - barRadius)
            barChartEntry.clickRect = clickRect
        } else {
            val clickRect = androidx.compose.ui.geometry.Rect(
                graphLeft + centerX - barWidth / 2,
                graphHeight - barChartEntry.total * unitPixels,
                graphLeft + centerX + barWidth / 2,
                graphHeight
            )
            if (clickRect.bottom - clickRect.top > 0) {
                cords.add(clickRect.left)
                cords.add(clickRect.top)
                cords.add(clickRect.right)
                cords.add(clickRect.bottom)
                barChartEntry.clickRect = clickRect
                barChartEntry.values.forEachIndexed { entryIndex, chartEntry ->
                    val thisValue = chartEntry.value
                    val value = barChartEntry.values.subList(entryIndex, barChartEntry.values.size)
                        .sumOf { it.value.toDouble() }.toFloat()
                    cords.add(graphLeft + centerX - barWidth / 2 + barStrokeWidth)
                    cords.add(graphHeight - value * unitPixels + if (entryIndex == 0) barStrokeWidth else 0f)
                    cords.add(graphLeft + centerX + barWidth / 2 - barStrokeWidth)
                    cords.add(graphHeight - (value - thisValue) * unitPixels)
                }
            }
        }
    }
    return cords
}

class BarChartMeasurement(
    var yAxisLabelWidth: Float = 0f,
    var axisLineWidth: Float = 0f,
    var dayLineWidth: Float = 0f,
    var xAxisLabelHeight: Float = 0f,
    var barRadius: Float = 0f,
    var innerBarRadius: Float = 0f,
    var barWidthRatioPercent: Float = 0f,
    var barStrokeWidth: Float = 0f,
    var dayLineDashPattern: FloatArray = floatArrayOf(),
    var graphWidth: Float = 0f,
    var viewWidth: Float = 0f,
    var graphHeight: Float = 0f,
    var valueFont: Int = 0,
    var valueTextSize: Float = 0f,
    var dayColumnWidth: Float = 0f,
    var maxValue: Int = 0,
    var hourRowHeight: Float = 0f,
    var xAxisLabelCenterY: Float = 0f,
    var yAxisLineCenterY: Float = 0f,
    var xAxisLineCenterX: Float = 0f,
    var valueCloudHeight: Float = 0f,
    var valueCloudBottomPadding: Float = 0f,
) {
    fun setupDefault(yAxisLabelWidthCustom: Dp, drawScope: DrawScope, chartData: BarChartDataSet) =
        with(drawScope) {
            yAxisLabelWidth = yAxisLabelWidthCustom.toPx()
            axisLineWidth = 2.dp.toPx()
            dayLineWidth = 1.dp.toPx()
            xAxisLabelHeight = 24.dp.toPx()
            graphWidth = size.width - yAxisLabelWidth - axisLineWidth
            graphHeight = size.height - axisLineWidth - xAxisLabelHeight
            dayColumnWidth = graphWidth / chartData.values.size
            barWidthRatioPercent = chartData.barRatio
            barRadius = minOf(8.dp.toPx(), dayColumnWidth * barWidthRatioPercent / 2)
            barStrokeWidth = 2.dp.toPx()
            innerBarRadius = barRadius - barStrokeWidth
        dayLineDashPattern = floatArrayOf(10.dp.toPx(), 10.dp.toPx())
        viewWidth = size.width
        valueFont = R.font.inter_semi_bold
        valueTextSize = 10.sp.toPx()
        maxValue = ceil(chartData.getMaxYValue() ?: DEF_BAR_CHART_Y_AXIS_MAX).toInt() + 1
        hourRowHeight = graphHeight / maxValue
        xAxisLabelCenterY = graphHeight + axisLineWidth + xAxisLabelHeight / 2
        yAxisLineCenterY = yAxisLabelWidth + axisLineWidth / 2
        xAxisLineCenterX = graphHeight + axisLineWidth / 2
        valueCloudHeight = 38.dp.toPx()
        valueCloudBottomPadding = 2.dp.toPx()
    }
}

private fun List<ChartEntry>.split(
    valuePerUnit: Float,
    minSplitValue: Float
): List<Pair<Float, Float>> {
    val sorted = distinctBy { it.value }.sortedBy { it.value }
    val pairs = arrayListOf<Pair<Float, Float>>()
    var first: Float? = null
    var prev: Float? = null
    sorted.forEach {
        if (first == null) {
            first = it.value
        } else if ((it.value - (prev ?: first ?: 0f)) * valuePerUnit >= minSplitValue) {
            pairs.add((first ?: 0f) to (prev ?: first ?: 0f))
            first = it.value
            prev = null
        } else {
            prev = it.value
        }
    }
    if (first != null) {
        pairs.add((first ?: 0f) to (prev ?: first ?: 0f))
    }
    return pairs
}

@Preview
@Composable
fun PreviewSleepChart() {
    val data = createMockWeeklySleepDetails().sleepDataSet
    BarChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(background),
        chartData = data,
        popupContent = { entry ->
            Text(
                modifier = Modifier.align(Center),
                text = data.cloudFormatter(entry).first
            )
        }
    )
}