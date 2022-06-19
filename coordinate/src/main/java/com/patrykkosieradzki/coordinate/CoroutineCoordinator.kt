package com.patrykkosieradzki.coordinate

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive

interface CoroutineCoordinator<R> : Coordinator<R> {
    val coroutineScope: CoroutineScope

    suspend fun start(): R
}

abstract class AbstractCoroutineCoordinator<R>(
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob()
) : CoroutineCoordinator<R> {

    override val coroutineScope = CoroutineScope(coroutineContext)

    private val _resultFlow by lazy {
        MutableSharedFlow<R>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    }

    protected fun finishWith(result: R) {
        _resultFlow.tryEmit(result)
        destroy()
    }

    override suspend fun start(): R {
        checkIfCanBeStarted()
        onStart()
        return _resultFlow.first()
    }

    private fun checkIfCanBeStarted() {
        check(coroutineScope.isActive) { "Cannot start a Coordinator that was finished" }
    }

    protected open fun onStart() {
        // Empty by default
    }

    override fun destroy() {
        coroutineScope.cancel()
    }
}
