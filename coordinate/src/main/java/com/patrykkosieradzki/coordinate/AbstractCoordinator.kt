package com.patrykkosieradzki.coordinate

import androidx.lifecycle.ViewModel

abstract class AbstractCoordinator : Coordinator {
    protected open val childCoordinator: Coordinator? = null

    final override fun <VM : ViewModel> createViewModel(vmClazz: Class<VM>): VM {
        if (childCoordinator != null) {
            return childCoordinator!!.createViewModel(vmClazz)
        }

        return onCreateViewModel(vmClazz)
    }

    open fun <VM : ViewModel> onCreateViewModel(vmClazz: Class<VM>): VM {
        throw NotImplementedError("Not implemented, define how ViewModels should be created")
    }

    override fun destroy() {
        childCoordinator?.destroy()
    }
}
