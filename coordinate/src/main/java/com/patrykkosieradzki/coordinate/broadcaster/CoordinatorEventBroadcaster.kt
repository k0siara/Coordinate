package com.patrykkosieradzki.coordinate.broadcaster

import androidx.fragment.app.Fragment
import com.patrykkosieradzki.coordinate.ktx.getCoordinatorHost
import com.patrykkosieradzki.coordinate.internal.launchInLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

interface CoordinatorEventBroadcaster<Event : Any> {
    val coordinatorEvents: Flow<Event>
    fun emitCoordinatorEvent(event: Event)

    companion object {
        fun <Event : Any> delegate(): CoordinatorEventBroadcaster<Event> {
            return CoordinatorEventBroadcasterImpl()
        }
    }
}

fun CoordinatorEventBroadcaster<*>.forwardCoordinatorEvents(fragment: Fragment) {
    val coordinator = fragment.getCoordinatorHost()?.coordinator
        ?: throw IllegalStateException("Could not find a CoordinatorHost")

    coordinatorEvents
        .onEach { event -> coordinator.emitEvent(event) }
        .launchInLifecycle(fragment.viewLifecycleOwner)
}
