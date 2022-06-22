package com.patrykkosieradzki.coordinate.core

import androidx.lifecycle.ViewModel

abstract class AbstractCoordinator<SCREEN>(
    private val navigator: Coordinator.Navigator<SCREEN>
) : Coordinator {

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

    final override fun emitEvent(event: Any) {
        navigator.canNavigate().takeIf { true } ?: return

        if (childCoordinator != null) {
            return childCoordinator!!.emitEvent(event)
        }

        return onEvent(event)
    }

    protected abstract fun onEvent(event: Any)

    override fun destroy() {
        childCoordinator?.destroy()
    }
}
