package com.patrykkosieradzki.coordinate

import androidx.lifecycle.ViewModel

interface Coordinator {

    interface Navigator<SCREEN> {
        // Just an example for now. Will add proper code later

        fun canNavigate(): Boolean

        fun navigateTo(screen: SCREEN)
        fun goBack()
    }

    fun <VM : ViewModel> createViewModel(vmClazz: Class<VM>): VM

    fun emitEvent(event: Any)

    fun destroy()
}
