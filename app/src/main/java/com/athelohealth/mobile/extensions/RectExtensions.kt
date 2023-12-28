package com.athelohealth.mobile.extensions

import android.graphics.RectF

fun RectF.update(
    left: Float = this.left,
    top: Float = this.top,
    right: Float = this.right,
    bottom: Float = this.bottom,
): RectF {
    set(left, top, right, bottom)
    return this
}