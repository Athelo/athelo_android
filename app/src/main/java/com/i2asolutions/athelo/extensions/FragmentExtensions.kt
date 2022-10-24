package com.i2asolutions.athelo.extensions

import android.content.Intent
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.appbar.AppBarLayout
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.presentation.ui.base.UpcomingScreen
import com.i2asolutions.athelo.presentation.ui.theme.AtheloTheme


fun FragmentActivity.getRootNavController(): NavController? {
    return (supportFragmentManager.fragments[0] as? NavHostFragment)?.navController
}

fun FragmentActivity.getChildNavController(): NavController? {
    return (supportFragmentManager.fragments.getOrNull(0)
        ?.childFragmentManager?.fragments?.getOrNull(0)
        ?.childFragmentManager?.fragments?.getOrNull(0) as? NavHostFragment)
        ?.navController
}

fun Fragment.shareContent(content: String?) {
    if (content.isNullOrBlank()) return
    activity?.let {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        it.startActivity(Intent.createChooser(shareIntent, "Share content using"))
    }
}

fun Fragment.createComposeContainerView(
    @ColorRes backgroundColorRes: Int = R.color.background,
    content: @Composable () -> Unit = { UpcomingScreen() }
): ComposeView {
    return ComposeView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            AppBarLayout.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(ContextCompat.getColor(context, backgroundColorRes))
        setContent { AtheloTheme(activity = requireActivity(), content = content) }
    }
}

fun DialogFragment.createComposeContainerView(
    @ColorRes backgroundColorRes: Int = R.color.background,
    content: @Composable () -> Unit = { UpcomingScreen() }
): ComposeView {
    return ComposeView(requireContext()).apply {
        setBackgroundColor(ContextCompat.getColor(context, backgroundColorRes))
        setContent { AtheloTheme(activity = requireActivity(), content = content) }
    }
}
