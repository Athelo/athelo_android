@file:OptIn(ExperimentalMaterial3Api::class)

package com.i2asolutions.athelo.presentation.ui.base.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.PathMeasure
import android.graphics.PointF
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.model.chart.EmptyChartEntry
import com.i2asolutions.athelo.presentation.model.chart.LineChartDataSet
import com.i2asolutions.athelo.presentation.model.chart.LineChartEntry
import com.i2asolutions.athelo.presentation.model.heartRate.EmptyHeartRateChartEntry
import com.i2asolutions.athelo.presentation.model.hrv.EmptyHrvChartEntry
import com.i2asolutions.athelo.presentation.ui.theme.gray
import com.i2asolutions.athelo.presentation.ui.theme.lightGray
import com.i2asolutions.athelo.presentation.ui.theme.white
import kotlin.math.ceil

const val DEX_LINE_CHART_Y_AXIS_MAX = 8f

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    chartData: LineChartDataSet,
    popupContent: @Composable BoxScope.(LineChartEntry) -> Unit
) {
    val context = LocalContext.current

    var shouldRecalculate = remember(chartData) { true }
    var path: Path = remember { Path() }
    var bgPath: Path = remember { Path() }
    val measurements: LineChartMeasurement = remember { LineChartMeasurement() }
    var clickedEntry: LineChartEntry? by remember(chartData) { mutableStateOf(null) }
    var cloudSize: IntSize by remember { mutableStateOf(IntSize(0, 0)) }
    val singleItemWidth = with(LocalDensity.current) { 9.dp.toPx() }
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
                measurements.setupDefault(this, chartData)
                path = prepareDataSet(
                    dataSet = chartData,
                    graphHeight = measurements.graphHeight,
                    graphLeft = measurements.yAxisLabelWidth + measurements.axisLineWidth / 2,
                    unitPixels = measurements.hourRowHeight,
                    placeForSingleBar = measurements.entryWidth,
                    singleItemWidth = singleItemWidth,
                )
                bgPath = prepareBgPath(path, measurements.graphHeight)
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
                        measurements.yAxisLabelWidth + measurements.axisLineWidth + measurements.entryWidth * i + measurements.entryWidth / 2
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
                        measurements.yAxisLabelWidth + measurements.axisLineWidth + measurements.entryWidth * i + measurements.entryWidth / 2
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
                    if (i != 0 && chartData.drawYLines)
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

            drawPath(
                path = bgPath,
                brush = Brush.verticalGradient(colors = chartData.backgroundGradient.map { Color(it) }),
                style = Fill
            )

            drawPath(
                path = path,
                color = Color(chartData.lineColor),
                style = Stroke(width = measurements.lineWidth)
            )
        }

        clickedEntry?.let { ce ->
            val indicatorX =
                LocalDensity.current.run { (ce.clickRect.center.x - 18.dp.toPx() / 2).toDp() }
            val indicatorY =
                LocalDensity.current.run { (ce.clickRect.top - 18.dp.toPx() / 2).toDp() }
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .offset(indicatorX, indicatorY)
                    .background(white, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(ce.bgColor), CircleShape)
                        .align(Center)
                )
            }

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
                    .offset(x = xDp, y = yDp)
                    .clip(RoundedCornerShape(12.dp))
                    .onSizeChanged {
                        cloudSize = it
                    },
                colors = CardDefaults.cardColors(white),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Center) {
                    popupContent(ce)
                }
            }
        }
    }
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
    dataSet: LineChartDataSet,
    graphHeight: Float,
    graphLeft: Float,
    unitPixels: Float,
    placeForSingleBar: Float,
    singleItemWidth: Float,
): Path {
    val path = Path()
    val points = arrayListOf<PointF>()
    val conPoint1 = arrayListOf<PointF>()
    val conPoint2 = arrayListOf<PointF>()
    var valuesStarted = false
    val isSingleValue =
        dataSet.values.count { it !is EmptyHeartRateChartEntry && it !is EmptyHrvChartEntry }
    dataSet.values.forEachIndexed { index, chartEntry ->
        if (chartEntry !is EmptyHeartRateChartEntry && chartEntry !is EmptyHrvChartEntry) {
            if (chartEntry is EmptyChartEntry || (valuesStarted && chartEntry.value == 0f)) return@forEachIndexed

            if (chartEntry.value > 0f) valuesStarted = true

            val x = graphLeft + placeForSingleBar * index + placeForSingleBar / 2
            val y = graphHeight - chartEntry.value * unitPixels
            points.add(PointF(x, y))
            if (isSingleValue == 1) {
                points.add(PointF(x + singleItemWidth, y))
            }

            chartEntry.clickRect =
                Rect(
                    Offset(x - placeForSingleBar / 2, y),
                    Offset(x + placeForSingleBar / 2, graphHeight)
                )
        }
    }
    for (i in 1 until points.size) {
        conPoint1.add(PointF((points[i].x + points[i - 1].x) / 2, points[i - 1].y))
        conPoint2.add(PointF((points[i].x + points[i - 1].x) / 2, points[i].y))
    }

    if (points.isEmpty() && conPoint1.isEmpty() && conPoint2.isEmpty()) return path

    path.reset()
    path.moveTo(points.first().x, points.first().y)

    for (i in 1 until points.size) {
        path.cubicTo(
            conPoint1[i - 1].x, conPoint1[i - 1].y, conPoint2[i - 1].x, conPoint2[i - 1].y,
            points[i].x, points[i].y
        )
    }
    return path
}

fun prepareBgPath(path: Path, graphBottom: Float): Path {
    val pm = PathMeasure(path.asAndroidPath(), false)
    val cords = floatArrayOf(0f, 0f)
    pm.getPosTan(0f, cords, null)
    val startPoint = cords.copyOf()
    pm.getPosTan(pm.length, cords, null)
    val endPoint = cords.copyOf()
    val closedPath = Path().apply { addPath(path) }
    closedPath.lineTo(endPoint[0], graphBottom)
    closedPath.lineTo(startPoint[0], graphBottom)
    closedPath.close()
    return closedPath
}

class LineChartMeasurement(
    var yAxisLabelWidth: Float = 0f,
    var axisLineWidth: Float = 0f,
    var dayLineWidth: Float = 0f,
    var xAxisLabelHeight: Float = 0f,
    var lineWidth: Float = 0f,
    var dayLineDashPattern: FloatArray = floatArrayOf(),
    var graphWidth: Float = 0f,
    var viewWidth: Float = 0f,
    var graphHeight: Float = 0f,
    var entryWidth: Float = 0f,
    var valueFont: Int = 0,
    var maxValue: Int = 0,
    var hourRowHeight: Float = 0f,
    var xAxisLabelCenterY: Float = 0f,
    var yAxisLineCenterY: Float = 0f,
    var xAxisLineCenterX: Float = 0f,
    var valueCloudHeight: Float = 0f,
    var valueCloudBottomPadding: Float = 0f,
) {
    fun setupDefault(drawScope: DrawScope, chartData: LineChartDataSet) = with(drawScope) {
        yAxisLabelWidth = 25.dp.toPx()
        axisLineWidth = 2.dp.toPx()
        dayLineWidth = 1.dp.toPx()
        xAxisLabelHeight = 24.dp.toPx()
        lineWidth = 2.dp.toPx()
        dayLineDashPattern = floatArrayOf(10.dp.toPx(), 10.dp.toPx())
        viewWidth = size.width
        graphWidth = size.width - yAxisLabelWidth - axisLineWidth
        graphHeight = size.height - axisLineWidth - xAxisLabelHeight
        valueFont = R.font.inter_semi_bold
        entryWidth = graphWidth / (chartData.xAxisMaxValue + 1) // + 1 for right margin
        maxValue = ceil(chartData.getMaxYValue() ?: DEX_LINE_CHART_Y_AXIS_MAX).toInt() + 1
        hourRowHeight = graphHeight / (maxValue + 1)
        xAxisLabelCenterY = graphHeight + axisLineWidth + xAxisLabelHeight / 2
        yAxisLineCenterY = yAxisLabelWidth + axisLineWidth / 2
        xAxisLineCenterX = graphHeight + axisLineWidth / 2
        valueCloudHeight = 38.dp.toPx()
        valueCloudBottomPadding = 10.dp.toPx()
    }
}

@Preview
@Composable
fun PreviewMonthSleepChart() {
    /*var data by remember {
        mutableStateOf(SleepMonthChartEntry.MOCK_MONTH)
    }
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(background),
        chartData = data,
        popupContent = { entry ->
            Text(
                modifier = Modifier.align(Center),
                text = data.cloudFormatter(entry)
            )
        }
    )*/
}