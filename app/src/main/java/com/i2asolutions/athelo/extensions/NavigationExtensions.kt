package com.i2asolutions.athelo.extensions

import android.graphics.Color
import android.net.Uri
import android.util.SparseIntArray
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.utils.CustomActionBarOnDestinationChangedListener


fun NavController.navigateWithPushAnimation(destinationId: Int) {
    navigate(
        destinationId, null, NavOptions.Builder()
            .setEnterAnim(R.anim.fragment_enter)
            .setExitAnim(R.anim.fragment_exit)
            .setPopEnterAnim(R.anim.fragment_pop_enter)
            .setPopExitAnim(R.anim.fragment_pop_exit)
            .setLaunchSingleTop(true)
            .build()
    )
}

fun NavController.navigateWithLeftPushAnimation(destinationId: Int) {
    navigate(
        destinationId, null, NavOptions.Builder()
            .setEnterAnim(R.anim.fragment_pop_enter)
            .setExitAnim(R.anim.fragment_pop_exit)
            .setPopEnterAnim(R.anim.fragment_enter)
            .setPopExitAnim(R.anim.fragment_exit)
            .setLaunchSingleTop(true)
            .build()
    )
}

val defaultNavOptionWithPushAnimation
    get() = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_enter)
        .setExitAnim(R.anim.fragment_exit)
        .setPopEnterAnim(R.anim.fragment_pop_enter)
        .setPopExitAnim(R.anim.fragment_pop_exit)

val splashNavOptionWithPushAnimation
    get() = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_enter)
        .setExitAnim(R.anim.fragment_exit)
        .setPopEnterAnim(R.anim.fragment_pop_enter)
        .setPopExitAnim(R.anim.fragment_pop_exit)
        .setPopUpTo(R.id.main_navigation, true)
        .build()

fun Fragment.popupTo(targetId: Int, include: Boolean) =
    findNavController().popBackStack(targetId, include)

fun Fragment.navigateTo(
    direction: NavDirections,
) {
    runCatching {
        findNavController().navigate(direction)
    }.onFailure {
        val mainNavController = requireActivity().findNavController(R.id.navHostFragment)
        mainNavController.navigate(direction)
    }
}

fun Fragment.navigateTo(
    deepLink: String,
    navOptions: NavOptions? = defaultNavOptionWithPushAnimation.build()
) {
    navigateTo(deepLink.toUri(), navOptions)
}

fun Fragment.navigateTo(
    deepLink: Uri,
    navOptions: NavOptions? = defaultNavOptionWithPushAnimation.build()
) {
    if (!runCatching { findNavController().graph.hasDeepLink(deepLink) }.getOrDefault(false)) {
        debugPrint("Not found in current graph")
        val mainNavController = requireActivity().findNavController(R.id.navHostFragment)
        if (mainNavController.graph.hasDeepLink(deepLink)) {
            mainNavController.navigate(deepLink, navOptions)
        } else {
            debugPrint("Not found in main graph")
        }
        return
    }
    if (navOptions != null) {
        findNavController().navigate(deepLink, navOptions)
    } else
        findNavController().navigate(deepLink)
}

fun AppCompatActivity.setupActionBarWithCustomNavController(
    navController: NavController,
    configuration: AppBarConfiguration = AppBarConfiguration(navController.graph),
) {
    navController.addOnDestinationChangedListener(
        CustomActionBarOnDestinationChangedListener(
            this,
            configuration,
            customIndicatorColor
        )
    )
}

val topLevelDestinations = setOf(
    R.id.homeFragment,
    R.id.sleepFragment,
    R.id.activityFragment,
    R.id.communityFragment,
    R.id.chatListFragment,
    R.id.newsFragment,
)
val communicationDestinations = setOf(
    R.id.chatListFragment,
)

fun MenuItem.getCustomSubViewSets() = when (itemId) {
    R.id.homeFragment -> setOf()
    R.id.sleepFragment -> setOf()
    R.id.activityFragment -> setOf()
    R.id.communityFragment -> communicationDestinations
    R.id.newsFragment -> setOf()
    else -> setOf()
}

private val customIndicatorColor = SparseIntArray().apply {

}

fun Fragment.openURL(url: String?) {
    if (url.isNullOrBlank()) return
    val builder = CustomTabsIntent.Builder()
    val primaryColor = ContextCompat.getColor(requireContext(), R.color.purple)
    val defaultColors = CustomTabColorSchemeParams.Builder()
        .setToolbarColor(primaryColor)
        .setToolbarColor(primaryColor)
        .setSecondaryToolbarColor(Color.WHITE)
        .build()
    builder.setDefaultColorSchemeParams(defaultColors)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(requireContext(), url.toUri())
}

fun Fragment.openMail(address: String) {
    val context = context ?: return
    ShareCompat.IntentBuilder(context).addEmailTo(address).startChooser()
}