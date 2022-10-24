package com.i2asolutions.athelo.presentation.ui.mainActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.i2asolutions.athelo.databinding.ActivityMainBinding
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.extensions.showSystemUI
import com.i2asolutions.athelo.utils.fitbit.FitbitConnectionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var fitbitConnectionHelper: FitbitConnectionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        viewModel.clearError()
        showSystemUI()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        setupNavigation()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        debugPrint("Hello new Intent! ${intent?.data ?: " null data "}")
        if (intent != null) {
            lifecycleScope.launch {
                fitbitConnectionHelper.parseResponse(intent)
            }
        }
    }

    override fun onPause() {
        viewModel.handleEvent(MainActivityEvent.DisconnectWebSockets)
        super.onPause()
    }

    override fun onResume() {
        viewModel.handleEvent(MainActivityEvent.ConnectWebSockets)
        super.onResume()
    }

    private fun setupNavigation() {
        //set action bar
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    fun openMenu() {
        binding.root.openDrawer(GravityCompat.START)
    }

    fun hideMenu(callback: () -> Unit) {
        binding.root.closeDrawer(GravityCompat.START)
        binding.root.addDrawerListener(object : DrawerLayout.DrawerListener {
            /**
             * Called when a drawer's position changes.
             * @param drawerView The child view that was moved
             * @param slideOffset The new offset of this drawer within its range, from 0-1
             */
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            /**
             * Called when a drawer has settled in a completely open state.
             * The drawer is interactive at this point.
             *
             * @param drawerView Drawer view that is now open
             */
            override fun onDrawerOpened(drawerView: View) {

            }

            /**
             * Called when a drawer has settled in a completely closed state.
             *
             * @param drawerView Drawer view that is now closed
             */
            override fun onDrawerClosed(drawerView: View) {
                callback()
                binding.root.removeDrawerListener(this)
            }

            /**
             * Called when the drawer motion state changes. The new state will
             * be one of [.STATE_IDLE], [.STATE_DRAGGING] or [.STATE_SETTLING].
             *
             * @param newState The new drawer motion state
             */
            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }

    internal inline fun <reified T> setupSingleUseCallback(
        key: String,
        crossinline body: (T?) -> Unit
    ) {
        val savedStateHandle = binding.navHostFragment.getFragment<NavHostFragment>()
            .findNavController().currentBackStackEntry?.savedStateHandle
        val liveData = savedStateHandle?.getLiveData<T>(key)
        liveData?.removeObservers(this)
        liveData?.observe(this) {
            body(it)
            liveData.removeObservers(this)
            savedStateHandle.remove<T>(key)
        }
    }
}