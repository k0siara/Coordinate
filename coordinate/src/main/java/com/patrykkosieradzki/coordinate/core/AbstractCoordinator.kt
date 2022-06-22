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
