package com.i2asolutions.athelo.utils

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.SparseIntArray
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.util.getOrDefault
import androidx.customview.widget.Openable
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import com.i2asolutions.athelo.R
import java.lang.ref.WeakReference


class CustomActionBarOnDestinationChangedListener(
    activity: AppCompatActivity,
    configuration: AppBarConfiguration,
    private val customIndicatorColorMap: SparseIntArray = SparseIntArray()
) : NavController.OnDestinationChangedListener {

    private var mActivity: AppCompatActivity? = activity

    private val mTopLevelDestinations: Set<Int> = configuration.topLevelDestinations
    private val mOpenableLayoutWeakReference: WeakReference<Openable>?
    private var mArrowDrawable: Drawable? = null

    @ColorRes
    private var mIndicatorColorRes: Int = R.color.lightPurple

    init {
        val openableLayout = configuration.openableLayout
        mOpenableLayoutWeakReference = if (openableLayout != null) {
            WeakReference(openableLayout)
        } else {
            null
        }
    }

    private fun setNavigationIcon(
        icon: Drawable?,
        @StringRes contentDescription: Int
    ) {
        val actionBar = mActivity?.supportActionBar
        if (icon == null) {
            actionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            actionBar?.setDisplayHomeAsUpEnabled(true)
            val delegate = mActivity?.drawerToggleDelegate
            delegate?.setActionBarUpIndicator(icon, contentDescription)
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (destination is FloatingWindow) {
            return
        }
        val openableLayout = mOpenableLayoutWeakReference?.get()
        if (mOpenableLayoutWeakReference != null && openableLayout == null) {
            controller.removeOnDestinationChangedListener(this)
            return
        }

        var isTopLevelDestination = mTopLevelDestinations.contains(destination.id)
        if (isTopLevelDestination && controller.currentBackStackEntry?.arguments?.get("show_back_arrow") == true) {
            isTopLevelDestination = false
        }

        if (openableLayout == null && isTopLevelDestination) {
            setNavigationIcon(null, 0)
        } else {
            setActionBarUpIndicator(
                showAsDrawerIndicator = openableLayout != null && isTopLevelDestination,
                indicatorColorRes = customIndicatorColorMap.getOrDefault(
                    destination.id,
                    R.color.lightPurple
                )
            )
        }

        mActivity?.supportActionBar?.title = ""
    }

    private fun setActionBarUpIndicator(
        showAsDrawerIndicator: Boolean,
        @ColorRes indicatorColorRes: Int
    ) {
        mActivity?.let { activity ->
            if (mArrowDrawable == null) {
                mArrowDrawable = ContextCompat.getDrawable(activity, R.drawable.ic_back_arrow)
            }

            if (mIndicatorColorRes != indicatorColorRes) {
                mIndicatorColorRes = indicatorColorRes
                mArrowDrawable?.let {
                    DrawableCompat.wrap(it).apply {
                        setTint(ContextCompat.getColor(activity, indicatorColorRes))
                    }
                }

            }

            setNavigationIcon(
                if (showAsDrawerIndicator) null else mArrowDrawable,
                R.string.app_name
            )
        }
    }
}