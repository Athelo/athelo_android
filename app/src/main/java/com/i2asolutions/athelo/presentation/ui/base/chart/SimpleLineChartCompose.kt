package com.i2asolutions.athelo.presentation.ui.base.chart

import android.graphics.PathMeasure
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.i2asolutions.athelo.presentation.ui.theme.background
import com.i2asolutions.athelo.presentation.ui.theme.lightOlivaceous

@Composable
fun SimpleLineChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    lineColor: Color = lightOlivaceous,
    bgGradient: List<Color> = listOf(Color(0x57D8EEB5), Color(0x00D8EEB5))
) {
    var shouldRecalculate = remember(data) { true }
    var path: Path = remember { Path() }
    var bgPath: Path = remember { Path() }
    val measurements: SimpleLineChartMeasurement = remember { SimpleLineChartMeasurement() }
    Box(modifier = modifier.fillMaxWidth()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            if (shouldRecalculate) {
                shouldRecalculate = false
                measurements.setupDefault(this)
                path = prepareDataSet(data, size, measurements)
                bgPath = prepareBgPath(path, size)
            }

            drawPath(
                path = bgPath,
                brush = Brush.verticalGradient(colors = bgGradient),
                style = Fill
            )

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = measurements.lineWidth)
            )
        }
    }
}

private fun prepareDataSet(
    data: List<Float>,
    size: Size,
    measurements: SimpleLineChartMeasurement
): Path {
    val path = Path()
    val points = arrayListOf<PointF>()
    val conPoint1 = arrayListOf<PointF>()
    val conPoint2 = arrayListOf<PointF>()
    data.forEachIndexed { index, value ->
        val x = measurements.step * index
        val y = size.height - size.height * value
        points.add(PointF(x, y))
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

private fun prepareBgPath(path: Path, size: Size): Path {
    val pm = PathMeasure(path.asAndroidPath(), false)
    val cords = floatArrayOf(0f, 0f)
    pm.getPosTan(0f, cords, null)
    val startPoint = cords.copyOf()
    pm.getPosTan(pm.length, cords, null)
    val endPoint = cords.copyOf()
    val closedPath = Path().apply { addPath(path) }
    closedPath.lineTo(endPoint[0], size.height)
    closedPath.lineTo(startPoint[0], size.height)
    closedPath.close()
    return closedPath
}

class SimpleLineChartMeasurement(
    var step: Float = 0f,
    var lineWidth: Float = 0f
) {
    fun setupDefault(drawScope: DrawScope) = with(drawScope) {
        lineWidth = 3.dp.toPx()
        step = size.width / 6
    }
}

@Preview
@Composable
fun PreviewSimpleLineChart() {
    SimpleLineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(background),
        data = listOf(0.6f, 0.7f, 0.8f, 0.5f, 0.4f, 0.5f, 0.7f),
        lineColor = lightOlivaceous,
        bgGradient = listOf(Color(0x57D8EEB5), Color(0x00D8EEB5))
    )
}