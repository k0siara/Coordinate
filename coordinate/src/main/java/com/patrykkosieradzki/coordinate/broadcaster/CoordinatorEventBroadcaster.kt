package com.patrykkosieradzki.coordinate.broadcaster

import androidx.fragment.app.Fragment
import com.patrykkosieradzki.coordinate.ktx.getCoordinatorHost
import com.patrykkosieradzki.coordinate.internal.launchInLifecycle
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

fun CoordinatorEventBroadcaster<*>.forwardCoordinatorEvents(fragment: Fragment) {
    val coordinator = fragment.getCoordinatorHost()?.coordinator
        ?: throw IllegalStateException("Could not find a CoordinatorHost")

    coordinatorEvents
        .onEach { event -> coordinator.emitEvent(event) }
        .launchInLifecycle(fragment.viewLifecycleOwner)
}
