package com.patrykkosieradzki.coordinate.sample.ui

import androidx.lifecycle.ViewModel
import com.patrykkosieradzki.coordinate.broadcaster.CoordinatorEventBroadcaster

class TestViewModel : ViewModel(),
    CoordinatorEventBroadcaster<Int> by CoordinatorEventBroadcaster.delegate() {
}
