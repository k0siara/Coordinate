package com.patrykkosieradzki.coordinate.core

import kotlinx.coroutines.CoroutineScope

interface CoroutineCoordinator<R> : Coordinator {
    val coroutineScope: CoroutineScope

    suspend fun start(): R
}
