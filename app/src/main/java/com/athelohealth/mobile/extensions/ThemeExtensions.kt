package com.athelohealth.mobile.extensions

import com.athelohealth.mobile.R
import com.athelohealth.mobile.widgets.TabLayout

fun TabLayout.useAppBaseConfiguration() {
    initStyle(
        selectedColorRes = R.color.gray,
        selectedFontRes = R.font.inter_medium,
        unselectedColorRes = R.color.lightGray,
        unselectedFontRes = R.font.inter_medium
    )
}