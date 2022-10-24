package com.i2asolutions.athelo.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.i2asolutions.athelo.extensions.scale
import com.i2asolutions.athelo.utils.consts.Const.TUTORIAL_SHAPE

class TutorialWelcomeShape : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = PathParser().parsePathString(TUTORIAL_SHAPE).scale(size).toPath()
        return Outline.Generic(path)
    }
}