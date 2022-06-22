/*
 * Copyright (C) 2022 Patryk Kosieradzki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrykkosieradzki.coordinate.ktx

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.patrykkosieradzki.coordinate.core.Coordinator
import com.patrykkosieradzki.coordinate.core.CoordinatorHost

@MainThread
inline fun <reified VM : ViewModel> Fragment.coordinatorViewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline coordinatorProducer: () -> Coordinator = defaultCoordinatorProducer
): Lazy<VM> {
    return createViewModelLazy(
        viewModelClass = VM::class,
        storeProducer = { ownerProducer().viewModelStore },
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return coordinatorProducer().createViewModel(modelClass)
                }
            }
        }
    )
}

val Fragment.defaultCoordinatorProducer: () -> Coordinator
    get() = {
        getCoordinatorHost()?.coordinator
            ?: throw IllegalStateException("Could not find a CoordinatorHost")
    }

private fun Fragment?.getCoordinatorHost(): CoordinatorHost? {
    if (this == null) return null
    (activity as? CoordinatorHost)?.let { return it }
    (this as? CoordinatorHost)?.let { return it }
    return parentFragment.getCoordinatorHost()
}
