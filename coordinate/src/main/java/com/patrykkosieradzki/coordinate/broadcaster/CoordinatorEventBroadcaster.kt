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
