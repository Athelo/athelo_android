package com.i2asolutions.athelo.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.i2asolutions.athelo.extensions.debugPrint
import com.i2asolutions.athelo.extensions.errorMessageOrUniversalMessage
import com.i2asolutions.athelo.extensions.parseMessage
import com.i2asolutions.athelo.utils.AuthorizationException
import com.i2asolutions.athelo.utils.conectivity.CONNECTED
import com.i2asolutions.athelo.utils.conectivity.DISCONNECTED
import com.i2asolutions.athelo.utils.conectivity.NetWorkDisconnectedException
import com.i2asolutions.athelo.utils.conectivity.NetWorkManager
import com.i2asolutions.athelo.utils.consts.Const
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<Ev, Ef> : ViewModel() where Ev : BaseEvent, Ef : BaseEffect {
    private val _messageStateFlow = MutableStateFlow<MessageState>(MessageState.NoMessageState(""))
    val errorStateFlow = _messageStateFlow.asSharedFlow()

    @Suppress("PropertyName")
    protected val _effect: MutableSharedFlow<Ef> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    var lastBlockedTimestamp = 0L
        private set

    protected val requestExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }

    abstract fun loadData()

    fun setupNetworkCallback() {
        NetWorkManager.networkCallback.onEach {
            debugPrint("No Internet status $it for class : ${this::class.simpleName}")
            when (it) {
                CONNECTED -> {
                    delay(300)
                    clearError()
                    delay(100)
                    loadData()
                }
                DISCONNECTED -> errorNoInternet()
            }
        }.launchIn(viewModelScope)
    }

    open fun handleEvent(event: Ev) {
        when (event) {
            is BaseEvent.DisplayMessage -> normalMessage(event.message)
            is BaseEvent.DisplayError -> errorMessage(event.error)
            is BaseEvent.DisplaySuccess -> successMessage(event.success)
            is BaseEvent.RefreshData -> loadData()
        }
    }

    fun sendBaseEvent(event: BaseEvent) {
        when (event) {
            is BaseEvent.DisplayMessage -> normalMessage(event.message)
            is BaseEvent.DisplayError -> errorMessage(event.error)
            is BaseEvent.DisplaySuccess -> successMessage(event.success)
            is BaseEvent.RefreshData -> loadData()
        }
    }

    open fun clearError(message: String = "") {
        launchOnUI {
            _messageStateFlow.emit(MessageState.NoMessageState(message))
        }
    }

    protected open fun handleError(throwable: Throwable) =
        when (throwable) {
            is AuthorizationException -> errorMessage(throwable.errorMessageOrUniversalMessage)
            is NetWorkDisconnectedException, is IOException -> errorNoInternet()
            is HttpException -> errorMessage(throwable.parseMessage())
            else -> errorMessage(throwable.errorMessageOrUniversalMessage)
        }

    protected open fun notifyEffectChanged(effect: Ef) {
        launchOnUI { _effect.emit(effect) }
    }


    protected open fun errorMessage(message: String) {
        launchOnUI {
            _messageStateFlow.emit(MessageState.ErrorMessageState(message))
        }
    }

    protected open fun normalMessage(message: String) {
        launchOnUI {
            _messageStateFlow.emit(MessageState.NormalMessageState(message))
        }
    }

    protected open fun successMessage(message: String) {
        launchOnUI {
            _messageStateFlow.emit(MessageState.SuccessMessageState(message))
        }
    }

    protected open fun errorNoInternet(message: String = Const.NO_INTERNET_MESSAGE) {
        launchOnUI {
            if (_messageStateFlow.value != MessageState.LowConnectivityMessageState(message))
                _messageStateFlow.emit(MessageState.LowConnectivityMessageState(message))
        }
    }

    protected fun launchRequest(
        context: CoroutineContext = Dispatchers.IO + requestExceptionHandler,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(context) {
            if (NetWorkManager.isDisconnected) return@launch
            clearError()
            block()
        }
    }

    protected fun launchOnUI(
        context: CoroutineContext = Dispatchers.Main,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(context) {
            block()
        }
    }

    /**
     * @return null if block not executed
     */
    fun <R> selfBlockRun(blockTime: Long = 1000L, block: () -> R): R? {
        val now = System.currentTimeMillis()
        return if (blockTime + lastBlockedTimestamp < now) {
            lastBlockedTimestamp = now
            block()
        } else {
            null
        }
    }
}
