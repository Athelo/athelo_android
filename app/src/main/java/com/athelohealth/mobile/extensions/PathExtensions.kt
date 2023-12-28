package com.athelohealth.mobile.extensions

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.PathParser

fun PathParser.scale(size: Size): PathParser {
    val path = this.toPath()
    val vScale = size.height / path.getBounds().height
    val hScale = size.width / path.getBounds().width
    val newNodes = ArrayList<PathNode>()
    this.toNodes().forEach { node ->
        newNodes.add(node.scaleNode(vScale, hScale))
    }
    return PathParser().addPathNodes(newNodes)
}

private fun PathNode.scaleNode(vScale: Float, hScale: Float) = when (this) {
    PathNode.Close -> PathNode.Close
    is PathNode.RelativeMoveTo -> PathNode.RelativeMoveTo(dx * hScale, dy * vScale)
    is PathNode.MoveTo -> PathNode.MoveTo(x * hScale, y * vScale)
    is PathNode.RelativeLineTo -> PathNode.RelativeLineTo(dx * hScale, dy * vScale)
    is PathNode.LineTo -> PathNode.LineTo(x * hScale, y * vScale)
    is PathNode.RelativeHorizontalTo -> PathNode.RelativeHorizontalTo(dx * hScale)
    is PathNode.HorizontalTo -> PathNode.HorizontalTo(x * hScale)
    is PathNode.RelativeVerticalTo -> PathNode.RelativeVerticalTo(dy * vScale)
    is PathNode.VerticalTo -> PathNode.VerticalTo(y * vScale)
    is PathNode.RelativeCurveTo -> PathNode.RelativeCurveTo(
        dx1 * hScale,
        dy1 * vScale,
        dx2 * hScale,
        dy2 * vScale,
        dx3 * hScale,
        dy3 * vScale
    )
    is PathNode.CurveTo -> PathNode.CurveTo(
        x1 * hScale,
        y1 * vScale,
        x2 * hScale,
        y2 * vScale,
        x3 * hScale,
        y3 * vScale
    )
    is PathNode.RelativeReflectiveCurveTo -> PathNode.RelativeReflectiveCurveTo(
        dx1 * hScale,
        dy1 * vScale,
        dx2 * hScale,
        dy2 * vScale,
    )
    is PathNode.ReflectiveCurveTo -> PathNode.ReflectiveCurveTo(
        x1 * hScale,
        y1 * vScale,
        x2 * hScale,
        y2 * vScale,
    )
    is PathNode.RelativeQuadTo -> PathNode.RelativeQuadTo(
        dx1 * hScale,
        dy1 * vScale,
        dx2 * hScale,
        dy2 * vScale,
    )
    is PathNode.QuadTo -> PathNode.QuadTo(
        x1 * hScale,
        y1 * vScale,
        x2 * hScale,
        y2 * vScale,
    )
    is PathNode.RelativeReflectiveQuadTo -> PathNode.RelativeReflectiveQuadTo(
        dx * hScale,
        dy * vScale
    )
    is PathNode.ReflectiveQuadTo -> PathNode.ReflectiveQuadTo(x * hScale, y * vScale)
    is PathNode.RelativeArcTo -> PathNode.RelativeArcTo(
        horizontalEllipseRadius * hScale,
        verticalEllipseRadius * vScale,
        theta,
        isMoreThanHalf,
        isPositiveArc,
        arcStartDx * hScale,
        arcStartDy * vScale
    )
    is PathNode.ArcTo -> PathNode.ArcTo(
        horizontalEllipseRadius * hScale,
        verticalEllipseRadius * vScale,
        theta,
        isMoreThanHalf,
        isPositiveArc,
        arcStartX * hScale,
        arcStartY * vScale
    )
}