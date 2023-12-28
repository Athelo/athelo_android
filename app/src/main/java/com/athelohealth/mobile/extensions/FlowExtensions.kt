package com.athelohealth.mobile.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

inline fun <reified T> Flow<T>.onEachCollect(
    lifecycle: Lifecycle,
    scope: LifecycleCoroutineScope,
    noinline action: suspend (T) -> Unit
) = onEach(action)
    .flowWithLifecycle(lifecycle)
    .launchIn(scope)

inline fun <reified T> Flow<T>.onEachCollect(
    owner: LifecycleOwner,
    noinline action: suspend (T) -> Unit
) = onEachCollect(
    lifecycle = owner.lifecycle,
    scope = owner.lifecycleScope,
    action = action
)