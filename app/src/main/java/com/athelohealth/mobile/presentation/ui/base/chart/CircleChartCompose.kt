package com.athelohealth.mobile.presentation.ui.base.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.athelohealth.mobile.presentation.model.chart.CircleChartDataSet
import com.athelohealth.mobile.presentation.model.sleep.SleepCircleChartEntry
import com.athelohealth.mobile.presentation.ui.theme.background
import com.athelohealth.mobile.presentation.ui.theme.subHeading

@Composable
fun CircleChart(
    modifier: Modifier = Modifier,
    chartData: CircleChartDataSet
) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            val bgCircleWidth = size.width * 0.11f
            val arcStrokeWidth = size.width * 0.14f

            drawCircle(
                color = Color(chartData.bgColor), style = Stroke(width = bgCircleWidth),
                center = Offset(size.width / 2, size.width / 2),
                radius = size.width / 2 - bgCircleWidth / 2
            )

            drawArc(
                color = chartData.valueColor,
                startAngle = -90f,
                sweepAngle = 360 * chartData.value,
                useCenter = false,
                style = Stroke(width = arcStrokeWidth, cap = StrokeCap.Round),
                size = Size(size.width - arcStrokeWidth, size.width - arcStrokeWidth),
                topLeft = Offset(arcStrokeWidth / 2, arcStrokeWidth / 2)
            )
        }

        Text(
            modifier = Modifier.align(Center),
            text = chartData.formattedValue,
            style = MaterialTheme.typography.subHeading.copy(
                fontSize = 16.sp,
                color = Color(chartData.textColor)
            ),
        )
    }
}

@Preview
@Composable
fun PreviewCircleSleepChart() {
    var data by remember {
        mutableStateOf(SleepCircleChartEntry.MOCK)
    }
    CircleChart(
        modifier = Modifier
            .size(100.dp)
            .background(background),
        chartData = data
    )
}