package com.i2asolutions.athelo.presentation.ui.containerWithTab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.i2asolutions.athelo.R
import com.i2asolutions.athelo.databinding.FragmentTabsBinding
import com.i2asolutions.athelo.extensions.getCustomSubViewSets
import com.i2asolutions.athelo.extensions.topLevelDestinations
import com.i2asolutions.athelo.presentation.model.home.Tabs
import com.i2asolutions.athelo.presentation.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference

@AndroidEntryPoint
class TabsFragment : BaseFragment<TabsViewModel>() {
    override val viewModel: TabsViewModel by viewModels()
    private lateinit var binding: FragmentTabsBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var firstTime = false

    override fun createContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTabsBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            setupNavigation()
            applyWindowInsets()
        }
    }

    fun routeToCommunity() {
        binding.bottomNavView.selectedItemId = R.id.chatListFragment
    }

    fun routeToNews() {
        binding.bottomNavView.selectedItemId = R.id.newsFragment
    }

    override fun setupObservers() {
    }


    private fun setupNavigation() {
        //set action bar
        val navView: BottomNavigationView = binding.bottomNavView
        val navController = binding.tabsNavHostFragment.getFragment<NavHostFragment>().navController
        appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        navView.setOnItemSelectedListener { item ->
            if (item.isChecked) {
                navController.popBackStack(item.itemId, false)
            } else
                NavigationUI.onNavDestinationSelected(
                    item,
                    navController
                )
        }
        val weakReference = WeakReference(navView)
        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                    view.menu.forEach { item ->
                        if (destination.hierarchy.any {
                                it.id == item.itemId || item.getCustomSubViewSets()
                                    .any { id -> id == it.id }
                            }) {
                            item.isChecked = true
                        }
                    }
                }
            })
        if (!firstTime) {
            navView.selectedItemId =
                when (arguments?.let { TabsFragmentArgs.fromBundle(it).tab }) {
                    Tabs.Home -> R.id.homeFragment
                    Tabs.Sleep -> R.id.sleepFragment
                    Tabs.Activity -> R.id.activityFragment
                    Tabs.Community -> R.id.chatListFragment
                    Tabs.News -> R.id.newsFragment
                    else -> R.id.homeFragment
                }
            firstTime = true
        }
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavView, null)

        ViewCompat.setOnApplyWindowInsetsListener(
            binding.root
        ) { _, insets ->
            (binding.root.layoutParams as? FrameLayout.LayoutParams)?.bottomMargin =
                insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            insets
        }
    }
}