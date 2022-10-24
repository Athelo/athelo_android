package com.i2asolutions.athelo.presentation.model.tutorial

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed interface TutorialPageItem {
    class FirstPage(
        @DrawableRes val bigLogo: Int? = null,
        @StringRes val message: Int? = null,
        @StringRes val buttonLabel: Int? = null,
    ) : TutorialPageItem

    class TutorialPage(
        @DrawableRes val smallLogo: Int? = null,
        @StringRes val title: Int? = null,
        @DrawableRes val bigLogo: Int? = null,
    ) : TutorialPageItem

    object LastPage : TutorialPageItem
}
