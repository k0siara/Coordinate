package com.patrykkosieradzki.coordinate.broadcaster

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class CoordinatorEventBroadcasterImpl<Event : Any> : CoordinatorEventBroadcaster<Event> {
    private val _coordinatorEvents: Channel<Event> by lazy { Channel(Channel.UNLIMITED) }
    override val coordinatorEvents: Flow<Event> by lazy { _coordinatorEvents.receiveAsFlow() }

    override fun emitCoordinatorEvent(event: Event) {
        _coordinatorEvents.trySend(event)
    }
}
