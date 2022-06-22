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
package com.patrykkosieradzki.coordinate.broadcaster

import androidx.fragment.app.Fragment
import com.patrykkosieradzki.coordinate.core.Coordinator
import com.patrykkosieradzki.coordinate.internal.launchInLifecycle
import com.patrykkosieradzki.coordinate.ktx.defaultCoordinatorProducer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

interface CoordinatorEventBroadcaster<EVENT : Any> {
    val coordinatorEvents: Flow<EVENT>
    fun emitCoordinatorEvent(event: EVENT)

    companion object {
        fun <EVENT : Any> delegate(): CoordinatorEventBroadcaster<EVENT> {
            return CoordinatorEventBroadcasterImpl()
        }
    }
}

fun CoordinatorEventBroadcaster<*>.forwardCoordinatorEvents(
    fragment: Fragment,
    coordinatorProducer: () -> Coordinator = fragment.defaultCoordinatorProducer
) {
    val coordinator = coordinatorProducer()

    coordinatorEvents
        .onEach { event -> coordinator.emitEvent(event) }
        .launchInLifecycle(fragment.viewLifecycleOwner)
}
