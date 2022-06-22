/*
 * Copyright (C) 2022 Patryk Kosieradzki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrykkosieradzki.coordinate.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext

abstract class AbstractCoroutineCoordinator<SCREEN, RESULT>(
    navigator: Coordinator.Navigator<SCREEN>,
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob()
) : AbstractCoordinator<SCREEN>(navigator = navigator), CoroutineCoordinator<RESULT> {

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
