package com.patrykkosieradzki.coordinate

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified VM : ViewModel> Fragment.coordinatorViewModels(): Lazy<VM> {
    return createViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = {
            val coordinator = getCoordinatorHost()?.coordinator
                ?: throw IllegalStateException("Could not find a CoordinatorHost")
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return coordinator.createViewModel(modelClass)
                }
            }
        }
    )
}

fun Fragment?.getCoordinatorHost(): CoordinatorHost? {
    if (this == null) return null
    if (this is CoordinatorHost) return this
    return parentFragment.getCoordinatorHost()
}
