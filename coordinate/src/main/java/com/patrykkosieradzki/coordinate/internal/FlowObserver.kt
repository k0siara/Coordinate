package com.patrykkosieradzki.coordinate.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class FlowObserver<T>(
    lifecycleOwner: LifecycleOwner,
    private val flow: Flow<T>
) {
    private var job: Job? = null
    private var observer: LifecycleEventObserver =
        LifecycleEventObserver { source: LifecycleOwner, event: Lifecycle.Event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    job = source.lifecycleScope.launch {
                        flow.collect()
                    }
                }
                Lifecycle.Event.ON_STOP -> {
                    job?.cancel()
                    job = null
                }
                else -> {
                    // Do nothing
                }
            }
        }

    init {
        lifecycleOwner.lifecycle.addObserver(observer)
    }
}

fun <T> Flow<T>.launchInLifecycle(
    lifecycleOwner: LifecycleOwner
) {
    FlowObserver(lifecycleOwner, this)
}
