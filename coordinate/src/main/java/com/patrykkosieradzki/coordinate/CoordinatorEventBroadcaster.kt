package com.patrykkosieradzki.coordinate

import androidx.fragment.app.Fragment
import com.patrykkosieradzki.coordinate.internal.launchInLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

interface CoordinatorEventBroadcaster<Event : Any> {
    val coordinatorEvents: Flow<Event>
    fun emitCoordinatorEvent(event: Event)
}

class CoordinatorEventBroadcasterImpl<Event : Any> : CoordinatorEventBroadcaster<Event> {
    private val _coordinatorEvents: Channel<Event> by lazy { Channel(Channel.UNLIMITED) }
    override val coordinatorEvents: Flow<Event> by lazy { _coordinatorEvents.receiveAsFlow() }

    override fun emitCoordinatorEvent(event: Event) {
        _coordinatorEvents.trySend(event)
    }
}

fun <Event : Any> coordinatorEventBroadcasterDelegate() = CoordinatorEventBroadcasterImpl<Event>()

fun CoordinatorEventBroadcaster<*>.forwardCoordinatorEvents(fragment: Fragment) {
    val coordinator = fragment.getCoordinatorHost()?.coordinator
        ?: throw IllegalStateException("Could not find a CoordinatorHost")

    coordinatorEvents
        .onEach { event -> coordinator.emitEvent(event) }
        .launchInLifecycle(fragment.viewLifecycleOwner)
}

