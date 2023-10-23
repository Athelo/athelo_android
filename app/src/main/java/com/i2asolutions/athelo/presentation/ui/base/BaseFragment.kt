package com.i2asolutions.athelo.presentation.ui.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.i2asolutions.athelo.extensions.onEachCollect
import com.i2asolutions.athelo.presentation.ui.mainActivity.MainActivity
import com.i2asolutions.athelo.presentation.ui.share.splash.SplashActivity

abstract class BaseFragment<VM> :
    Fragment() where VM : BaseViewModel<*, *> {
    private var currentAlertDialog: AlertDialog? = null
    protected abstract val viewModel: VM

    fun takeOverBackPress(): Boolean {
        return false
    }

    protected abstract fun createContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createContentView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupError()
        setupObservers()
        lifecycleScope.launchWhenCreated {
            viewModel.setupNetworkCallback()
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    abstract fun setupObservers()

    private fun setupError() {
        viewModel.effect.onEachCollect(viewLifecycleOwner) {
            when (it) {
                BaseEffect.ShowAuthorizationScreen -> requireActivity().let { activity ->
                    activity.finishAffinity()
                    startActivity(Intent(activity, SplashActivity::class.java))
                }
            }
        }
    }

    protected fun hideKeyboard() {
        activity?.let {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(it.currentFocus?.windowToken, 0)
        }
    }

    protected fun showKeyboard() {
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(view, 0)
    }

    protected fun showAlertDialog(
        title: String,
        message: CharSequence,
        positiveButtonText: String,
        negativeButtonText: String? = null,
        positiveButtonListener: ((dialog: DialogInterface) -> Unit)? = null,
        negativeButtonListener: ((dialog: DialogInterface) -> Unit)? = null
    ) {
        if (currentAlertDialog != null) return
        if (isAdded) {
            activity?.let { hostActivity ->
                val builder = AlertDialog.Builder(hostActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(positiveButtonText) { dialog, _ ->
                        dialog.dismiss()
                        if (positiveButtonListener != null) positiveButtonListener(dialog)
                    }
                if (negativeButtonText?.isBlank() == false) {
                    builder
                        .setNegativeButton(negativeButtonText) { dialog, _ ->
                            dialog.dismiss()
                            if (negativeButtonListener != null) negativeButtonListener(dialog)
                        }
                }
                currentAlertDialog = builder
                    .setOnDismissListener {
                        currentAlertDialog = null
                    }.show()
            }
        }
    }

    protected fun showToBeImplementedMessage() {
        viewModel.sendBaseEvent(BaseEvent.DisplayMessage("To be implemented"))
    }

    protected fun showSnackbar(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun hideMenu(callback: () -> Unit) {
        (activity as? MainActivity)?.hideMenu(callback)
    }

    protected fun openMenu() {
        (activity as? MainActivity)?.openMenu()
    }
}