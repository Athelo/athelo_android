package com.athelohealth.mobile.presentation.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.athelohealth.mobile.extensions.debugPrint
import com.athelohealth.mobile.extensions.errorMessageOrUniversalMessage
import com.athelohealth.mobile.extensions.parseMessage
import com.athelohealth.mobile.utils.AuthorizationException
import com.athelohealth.mobile.utils.conectivity.CONNECTED
import com.athelohealth.mobile.utils.conectivity.DISCONNECTED
import com.athelohealth.mobile.utils.conectivity.NetWorkDisconnectedException
import com.athelohealth.mobile.utils.conectivity.NetWorkManager
import com.athelohealth.mobile.utils.consts.Const
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.CoroutineContext
abstract class BaseViewModel<Ev, Ef, st>(state: st) : ViewModel() where Ev : BaseEvent, Ef : BaseEffect, st: BaseViewState {
    private val _messageStateFlow = MutableStateFlow<MessageState>(MessageState.NoMessageState(""))
    val errorStateFlow = _messageStateFlow.asSharedFlow()

    @Suppress("PropertyName")
    protected val _effect: MutableSharedFlow<Ef> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    private val _baseEffect: MutableSharedFlow<BaseEffect> = MutableSharedFlow()
    val baseEffect = _baseEffect.asSharedFlow()


    protected var currentState: st
    init {
        currentState = state
    }

    private val _viewState = MutableStateFlow(currentState)
    val viewState = _viewState.asStateFlow()

    var lastBlockedTimestamp = 0L
        private set

    protected val requestExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        }

    abstract fun pauseLoadingState()

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

    protected open fun handleError(throwable: Throwable) {
        pauseLoadingState()
        when (throwable) {
            is AuthorizationException -> handleAuthorizationException(throwable)
            is NetWorkDisconnectedException, is IOException -> errorNoInternet()
            is HttpException -> errorMessage(throwable.parseMessage())
            else -> errorMessage(throwable.errorMessageOrUniversalMessage)
        }
    }

    private fun handleAuthorizationException(authException: AuthorizationException) {
        val throwable = authException.throwable

        val authError = MessageState.AuthorizationErrorMessageState(message = authException.errorMessageOrUniversalMessage)
        if (throwable is HttpException) {
            when(throwable.code()) {
                401 -> {
                    logoutEffect()
                }
                else -> {
                    errorMessage(authError)
                }
            }
        } else {
            errorMessage(authException.errorMessageOrUniversalMessage)
        }
    }


    protected open fun notifyEffectChanged(effect: Ef) {
        launchOnUI { _effect.emit(effect) }
    }


    protected open fun errorMessage(message: String) {
        pauseLoadingState()
        launchOnUI {
            _messageStateFlow.emit(MessageState.ErrorMessageState(message))
        }
    }

    protected open fun errorMessage(messageState: MessageState) {
        pauseLoadingState()
        launchOnUI {
            _messageStateFlow.emit(messageState)
        }
    }

    fun logoutEffect() {
        launchOnUI { _baseEffect.emit(BaseEffect.ShowAuthorizationScreen) }
    }

    protected open fun notifyStateChange(currentState: st) {
        this.currentState = currentState
        launchOnUI {
            _viewState.emit(this.currentState)
        }
    }

    protected open fun notifyStateChange() {
        launchOnUI { _viewState.emit(currentState) }
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