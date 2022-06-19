package com.patrykkosieradzki.coordinate

interface Coordinator<R> {

    interface Navigator<S> {
        fun navigateTo(screen: S)
        fun goBack()
    }

    fun emitEvent(event: Any) {
        // Empty by default
    }

    fun destroy()
}