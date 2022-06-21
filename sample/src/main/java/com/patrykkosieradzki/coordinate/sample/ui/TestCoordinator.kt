package com.patrykkosieradzki.coordinate.sample.ui

import com.patrykkosieradzki.coordinate.AbstractCoroutineCoordinator
import com.patrykkosieradzki.coordinate.Coordinator

class TestCoordinator : AbstractCoroutineCoordinator<Int>() {

    override val childCoordinator: Coordinator?
        get() = super.childCoordinator
}