package com.patrykkosieradzki.coordinate

import kotlinx.coroutines.CoroutineScope

interface CoroutineCoordinator<R> : Coordinator {
    val coroutineScope: CoroutineScope

    suspend fun start(): R
}
