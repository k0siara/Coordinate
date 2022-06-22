package com.patrykkosieradzki.coordinate.sample.ui

import androidx.lifecycle.ViewModel
import com.patrykkosieradzki.coordinate.broadcaster.CoordinatorEventBroadcaster
import com.patrykkosieradzki.coordinate.sample.ui.TestViewModel.CoordinatorEvent

class TestViewModel : ViewModel(),
    CoordinatorEventBroadcaster<CoordinatorEvent> by CoordinatorEventBroadcaster.delegate() {

    sealed class CoordinatorEvent {
        object EventA : CoordinatorEvent()
    }

    init {
        emitCoordinatorEvent(CoordinatorEvent.EventA)
    }
}
