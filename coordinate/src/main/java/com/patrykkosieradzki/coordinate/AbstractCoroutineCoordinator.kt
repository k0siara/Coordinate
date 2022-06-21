package com.patrykkosieradzki.coordinate

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

abstract class AbstractCoroutineCoordinator<RESULT>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob()
) : AbstractCoordinator(), CoroutineCoordinator<RESULT> {

    override val coroutineScope = CoroutineScope(coroutineContext)

    private val _resultChannel: Channel<RESULT> by lazy {
        Channel(capacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    }
    private val _wasStarted by lazy { MutableStateFlow(false) }

    final override suspend fun start(): RESULT {
        checkIfCanBeStarted()
        _wasStarted.update { true }
        onStart()
        return _resultChannel.receive()
    }

    private fun checkIfCanBeStarted() {
        check(_wasStarted.value.not()) { "Coordinator was already started" }
        check(coroutineScope.isActive) { "Cannot start a Coordinator that was finished" }
    }

    protected open fun onStart() {
        // Empty by default
    }

    protected fun finishWith(result: RESULT) {
        _resultChannel.trySend(result)
        destroy()
    }

    override fun destroy() {
        _resultChannel.close(Error("Coordinator was already finished or destroyed"))
        childCoordinator?.destroy()
        coroutineScope.cancel()
    }
}
