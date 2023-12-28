package com.athelohealth.mobile.presentation.ui.base.menu

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider
import com.athelohealth.mobile.presentation.ui.theme.darkPurple
import com.athelohealth.mobile.presentation.ui.theme.gray
import com.athelohealth.mobile.presentation.ui.theme.lightGray
import com.athelohealth.mobile.presentation.ui.theme.white
import kotlin.math.max
import kotlin.math.min


@Suppress("ModifierParameter")
@Composable
internal fun DropdownMenuContent(
    expandedStates: MutableTransitionState<Boolean>,
    transformOriginState: MutableState<TransformOrigin>,
    modifier: Modifier = Modifier,
    showScrollBar: Boolean = false,
    maxHeight: Dp,
    cornerRadius: Dp = 30.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Menu open/close animation.
    val transition = updateTransition(expandedStates, "DropDownMenu")

    val scale by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(
                    durationMillis = InTransitionDuration,
                    easing = LinearOutSlowInEasing
                )
            } else {
                // Expanded to dismissed.
                tween(
                    durationMillis = 1,
                    delayMillis = OutTransitionDuration - 1
                )
            }
        }, label = ""
    ) {
        if (it) {
            // Menu is expanded.
            1f
        } else {
            // Menu is dismissed.
            0.8f
        }
    }

    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                // Dismissed to expanded
                tween(durationMillis = 30)
            } else {
                // Expanded to dismissed.
                tween(durationMillis = OutTransitionDuration)
            }
        }, label = ""
    ) {
        if (it) {
            // Menu is expanded.
            1f
        } else {
            // Menu is dismissed.
            0f
        }
    }
    Surface(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
                clip = false
                transformOrigin = transformOriginState.value
            },
        color = Color.Transparent,
    ) {
        val scrollState = rememberScrollState()
        Column(
            content = content,
            modifier = modifier
                .shadow(2.dp, RoundedCornerShape(cornerRadius))
                .background(white, RoundedCornerShape(cornerRadius))
                .padding(vertical = DropdownMenuVerticalPadding)
                .width(IntrinsicSize.Max)
                .verticalScroll(scrollState)
                .drawWithContent {
                    drawContent()
                    val firstVisibleElementIndex = scrollState.value
                    val needDrawScrollbar = scrollState.isScrollInProgress || alpha > 0.0f

                    // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
                    if (needDrawScrollbar && showScrollBar) {
                        val elementHeight =
                            (this.size.height - 16.dp.toPx()) / (scrollState.maxValue + 2)
                        val scrollbarOffsetY =
                            (firstVisibleElementIndex) * elementHeight
                        val width = 16.dp
                        val scrollbarOffset =
                            (2 * DropdownMenuVerticalPadding).toPx() + scrollbarOffsetY - (56.dp.toPx() * (maxHeight.toPx() / size.height))
                        val minScrollbarOffset = (2 * DropdownMenuVerticalPadding).toPx()
                        drawRoundRect(
                            color = lightGray.copy(alpha = 0.3f),
                            topLeft = Offset(
                                size.width - width.toPx() - 16.dp.toPx(),
                                (2 * DropdownMenuVerticalPadding).toPx()
                            ),
                            size = Size(
                                width = width.toPx(),
                                height = size.height - (4 * DropdownMenuVerticalPadding).toPx()
                            ),
                            cornerRadius = CornerRadius(13.dp.toPx(), 13.dp.toPx())
                        )
                        drawRoundRect(
                            color = darkPurple,
                            topLeft = Offset(
                                size.width - width.toPx() - 16.dp.toPx(),
                                max(minScrollbarOffset, scrollbarOffset)
                            ),
                            size = Size(
                                width = width.toPx(),
                                height = 56.dp.toPx() * (maxHeight.toPx() / size.height)
                            ),
                            cornerRadius = CornerRadius(13.dp.toPx(), 13.dp.toPx())
                        )
                    }
                }
                .composed {
                    if (showScrollBar)
                        Modifier.padding(end = 24.dp)
                    else
                        Modifier
                }
        )
    }
}

@Composable
internal fun DropdownMenuItemContent(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    enabled: Boolean,
    colors: MenuItemColors,
    contentPadding: PaddingValues,
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = onClick,
                interactionSource = interactionSource,
                indication = rememberRipple(true)
            )
            .fillMaxWidth()
            // Preferred min and max width used during the intrinsic measurement.
            .sizeIn(
                minWidth = DropdownMenuItemDefaultMinWidth,
                maxWidth = DropdownMenuItemDefaultMaxWidth,
                minHeight = 30.dp
            )
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
            if (leadingIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.leadingIconColor(enabled).value,
                ) {
                    Box(Modifier.defaultMinSize(minWidth = 56.dp)) {
                        leadingIcon()
                    }
                }
            }
            CompositionLocalProvider(LocalContentColor provides colors.textColor(enabled).value) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(
                            start = if (leadingIcon != null) {
                                DropdownMenuItemHorizontalPadding
                            } else {
                                0.dp
                            },
                            end = if (trailingIcon != null) {
                                DropdownMenuItemHorizontalPadding
                            } else {
                                0.dp
                            }
                        )
                ) {
                    text()
                }
            }
            if (trailingIcon != null) {
                CompositionLocalProvider(
                    LocalContentColor provides colors.trailingIconColor(enabled).value
                ) {
                    Box(Modifier.defaultMinSize(minWidth = 56.dp)) {
                        trailingIcon()
                    }
                }
            }
        }
    }
}

/**
 * Contains default values used for [DropdownMenuItem].
 */
object MenuDefaults {

    /**
     * Creates a [MenuItemColors] that represents the default text and icon colors used in a
     * [DropdownMenuItemContent].
     *
     * @param textColor the text color of this [DropdownMenuItemContent] when enabled
     * @param leadingIconColor the leading icon color of this [DropdownMenuItemContent] when enabled
     * @param trailingIconColor the trailing icon color of this [DropdownMenuItemContent] when
     * enabled
     * @param disabledTextColor the text color of this [DropdownMenuItemContent] when not enabled
     * @param disabledLeadingIconColor the leading icon color of this [DropdownMenuItemContent] when
     * not enabled
     * @param disabledTrailingIconColor the trailing icon color of this [DropdownMenuItemContent]
     * when not enabled
     */
    @Composable
    fun itemColors(
        textColor: Color = gray,
        leadingIconColor: Color = gray,
        trailingIconColor: Color = gray,
        disabledTextColor: Color =
            lightGray,
        disabledLeadingIconColor: Color = gray
            .copy(alpha = 0.5f),
        disabledTrailingIconColor: Color = gray
            .copy(alpha = 0.5f),
    ): MenuItemColors =
        DefaultMenuItemColors(
            textColor = textColor,
            leadingIconColor = leadingIconColor,
            trailingIconColor = trailingIconColor,
            disabledTextColor = disabledTextColor,
            disabledLeadingIconColor = disabledLeadingIconColor,
            disabledTrailingIconColor = disabledTrailingIconColor,
        )

    /**
     * Default padding used for [DropdownMenuItem].
     */
    val DropdownMenuItemContentPadding = PaddingValues(
        horizontal = DropdownMenuItemHorizontalPadding,
        vertical = 0.dp
    )

    /**
     * Default [Divider], which can be optionally positioned at the bottom of the
     * [DropdownMenuItemContent].
     *
     * @param modifier modifier for the divider's layout
     * @param color color of the divider
     * @param thickness thickness of the divider
     */
    @Composable
    fun Divider(
        modifier: Modifier = Modifier,
        color: Color = gray,
        thickness: Dp = 1.dp,
    ) {
        androidx.compose.material3.Divider(
            modifier = modifier,
            color = color,
            thickness = thickness,
        )
    }
}

/**
 * Represents the text and icon colors used in a menu item at different states.
 *
 * - See [MenuDefaults.itemColors] for the default colors used in a [DropdownMenuItemContent].
 */
@Stable
interface MenuItemColors {

    /**
     * Represents the text color for a menu item, depending on its [enabled] state.
     *
     * @param enabled whether the menu item is enabled
     */
    @Composable
    fun textColor(enabled: Boolean): State<Color>

    /**
     * Represents the leading icon color for a menu item, depending on its [enabled] state.
     *
     * @param enabled whether the menu item is enabled
     */
    @Composable
    fun leadingIconColor(enabled: Boolean): State<Color>

    /**
     * Represents the trailing icon color for a menu item, depending on its [enabled] state.
     *
     * @param enabled whether the menu item is enabled
     */
    @Composable
    fun trailingIconColor(enabled: Boolean): State<Color>
}

internal fun calculateTransformOrigin(
    parentBounds: IntRect,
    menuBounds: IntRect
): TransformOrigin {
    val pivotX = when {
        menuBounds.left >= parentBounds.right -> 0f
        menuBounds.right <= parentBounds.left -> 1f
        menuBounds.width == 0 -> 0f
        else -> {
            val intersectionCenter =
                (
                        max(parentBounds.left, menuBounds.left) +
                                min(parentBounds.right, menuBounds.right)
                        ) / 2
            (intersectionCenter - menuBounds.left).toFloat() / menuBounds.width
        }
    }
    val pivotY = when {
        menuBounds.top >= parentBounds.bottom -> 0f
        menuBounds.bottom <= parentBounds.top -> 1f
        menuBounds.height == 0 -> 0f
        else -> {
            val intersectionCenter =
                (
                        max(parentBounds.top, menuBounds.top) +
                                min(parentBounds.bottom, menuBounds.bottom)
                        ) / 2
            (intersectionCenter - menuBounds.top).toFloat() / menuBounds.height
        }
    }
    return TransformOrigin(pivotX, pivotY)
}

// Menu positioning.

/**
 * Calculates the position of a Material [DropdownMenu].
 */
@Immutable
internal data class DropdownMenuPositionProvider(
    val contentOffset: DpOffset,
    val density: Density,
    val maxHeight: Dp,
    val onPositionCalculated: (IntRect, IntRect) -> Unit = { _, _ -> }
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        // The min margin above and below the menu, relative to the screen.
        val verticalMargin = with(density) { MenuVerticalMargin.roundToPx() }
        // The content offset specified using the dropdown offset parameter.
        val contentOffsetX = with(density) { contentOffset.x.roundToPx() }
        val contentOffsetY = with(density) { contentOffset.y.roundToPx() }
        val popupHeight =
            with(density) { if (maxHeight > 0.dp) maxHeight.roundToPx() else popupContentSize.height }
        // Compute horizontal position.
        val toRight = anchorBounds.left + contentOffsetX
        val toLeft = anchorBounds.right - contentOffsetX - popupContentSize.width
        val toDisplayRight = windowSize.width - popupContentSize.width
        val toDisplayLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(
                toRight,
                toLeft,
                // If the anchor gets outside of the window on the left, we want to position
                // toDisplayLeft for proximity to the anchor. Otherwise, toDisplayRight.
                if (anchorBounds.left >= 0) toDisplayRight else toDisplayLeft
            )
        } else {
            sequenceOf(
                toLeft,
                toRight,
                // If the anchor gets outside of the window on the right, we want to position
                // toDisplayRight for proximity to the anchor. Otherwise, toDisplayLeft.
                if (anchorBounds.right <= windowSize.width) toDisplayLeft else toDisplayRight
            )
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: toLeft

        // Compute vertical position.
        val toBottom = maxOf(anchorBounds.bottom + contentOffsetY, verticalMargin)
        val toTop = anchorBounds.top - contentOffsetY - popupHeight
        val toCenter = anchorBounds.top - popupHeight / 2
        val toDisplayBottom = windowSize.height - popupHeight - verticalMargin
        val y = sequenceOf(toBottom, toTop, toCenter, toDisplayBottom).firstOrNull {
            it >= verticalMargin &&
                    it + popupHeight <= windowSize.height - verticalMargin
        } ?: toTop

        onPositionCalculated(
            anchorBounds,
            IntRect(x, y, x + popupContentSize.width, y + popupHeight)
        )
        return IntOffset(x, y)
    }
}

/** Default [MenuItemColors] implementation. */
@Immutable
private class DefaultMenuItemColors(
    private val textColor: Color,
    private val leadingIconColor: Color,
    private val trailingIconColor: Color,
    private val disabledTextColor: Color,
    private val disabledLeadingIconColor: Color,
    private val disabledTrailingIconColor: Color,
) : MenuItemColors {

    @Composable
    override fun textColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) textColor else disabledTextColor)
    }

    @Composable
    override fun leadingIconColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) leadingIconColor else disabledLeadingIconColor)
    }

    @Composable
    override fun trailingIconColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) trailingIconColor else disabledTrailingIconColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DefaultMenuItemColors

        if (textColor != other.textColor) return false
        if (leadingIconColor != other.leadingIconColor) return false
        if (trailingIconColor != other.trailingIconColor) return false
        if (disabledTextColor != other.disabledTextColor) return false
        if (disabledLeadingIconColor != other.disabledLeadingIconColor) return false
        if (disabledTrailingIconColor != other.disabledTrailingIconColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = textColor.hashCode()
        result = 31 * result + leadingIconColor.hashCode()
        result = 31 * result + trailingIconColor.hashCode()
        result = 31 * result + disabledTextColor.hashCode()
        result = 31 * result + disabledLeadingIconColor.hashCode()
        result = 31 * result + disabledTrailingIconColor.hashCode()
        return result
    }
}

// Size defaults.
internal val MenuVerticalMargin = 32.dp
private val DropdownMenuItemHorizontalPadding = 12.dp
internal val DropdownMenuVerticalPadding = 8.dp
private val DropdownMenuItemDefaultMinWidth = 112.dp
private val DropdownMenuItemDefaultMaxWidth = 280.dp

// Menu open/close animation.
internal const val InTransitionDuration = 120
internal const val OutTransitionDuration = 75
