package com.patrykkosieradzki.coordinate.sample.ui

import com.patrykkosieradzki.coordinate.core.AbstractCoroutineCoordinator
import com.patrykkosieradzki.coordinate.core.Coordinator
import com.patrykkosieradzki.coordinate.sample.ui.TestCoordinator.Screen

class TestCoordinator(
    private val testNavigator: TestNavigator
) : AbstractCoroutineCoordinator<Screen, Int>(navigator = testNavigator) {

    sealed class Screen {
        object A : Screen()
        object B : Screen()
    }

    override fun onEvent(event: Any) {
        when (event) {
            TestViewModel.CoordinatorEvent.EventA -> {
                testNavigator.navigateTo(Screen.B)
                finishWith(123)
            }
        }
    }
}

class TestNavigator : Coordinator.Navigator<Screen> {

    override fun canNavigate(): Boolean {
        return true
    }

    override fun navigateTo(screen: Screen) {
        TODO("Not yet implemented")
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }
}
