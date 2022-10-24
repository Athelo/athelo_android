package com.i2asolutions.athelo.extensions

import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.widgets.TabLayout

fun TabLayout.useAppBaseConfiguration() {
    initStyle(
        selectedColorRes = R.color.gray,
        selectedFontRes = R.font.inter_medium,
        unselectedColorRes = R.color.lightGray,
        unselectedFontRes = R.font.inter_medium
    )
}