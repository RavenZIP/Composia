package io.github.ravenzip.composia.control.activation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActivationControlTest {
    @Test
    fun `initial state is Enabled when not disabled`() = runTest {
        val control = mutableActivationControlOf(coroutineScope = backgroundScope)

        assertTrue(control.isEnabled)
        assertFalse(control.isDisabled)
    }

    @Test
    fun `initial state is Disabled when disabled`() = runTest {
        val control = mutableActivationControlOf(enabled = false, coroutineScope = backgroundScope)

        assertTrue(control.isDisabled)
        assertFalse(control.isEnabled)
    }

    @Test
    fun `state changed to Disabled`() = runTest {
        val control = mutableActivationControlOf(coroutineScope = backgroundScope)
        control.disable()

        assertTrue(control.isDisabled)
        assertFalse(control.isEnabled)
    }

    @Test
    fun `state changed to Enabled`() = runTest {
        val control = mutableActivationControlOf(enabled = false, coroutineScope = backgroundScope)
        control.enable()

        assertTrue(control.isEnabled)
        assertFalse(control.isDisabled)
    }

    @Test
    fun `reset restores initial state`() = runTest {
        val enabledControl = mutableActivationControlOf(coroutineScope = backgroundScope)

        enabledControl.disable()
        assertTrue(enabledControl.isDisabled)
        assertFalse(enabledControl.isEnabled)

        enabledControl.reset()
        assertTrue(enabledControl.isEnabled)
        assertFalse(enabledControl.isDisabled)

        val disabledControl =
            mutableActivationControlOf(enabled = false, coroutineScope = backgroundScope)

        disabledControl.enable()
        assertTrue(disabledControl.isEnabled)
        assertFalse(disabledControl.isDisabled)

        disabledControl.reset()
        assertTrue(disabledControl.isDisabled)
        assertFalse(disabledControl.isEnabled)
    }

    @Test
    fun `isDisabledFlow emits changes`() = runTest {
        turbineScope {
            val control = mutableActivationControlOf(coroutineScope = backgroundScope)

            control.isDisabledFlow.test {
                assertFalse(awaitItem())

                control.disable()
                assertTrue(awaitItem())

                control.enable()
                assertFalse(awaitItem())
            }
        }
    }

    @Test
    fun `isEnabledFlow emits changes`() = runTest {
        turbineScope {
            val control =
                mutableActivationControlOf(enabled = false, coroutineScope = backgroundScope)

            control.isEnabledFlow.test {
                assertFalse(awaitItem())

                control.enable()
                assertTrue(awaitItem())

                control.disable()
                assertFalse(awaitItem())
            }
        }
    }
}
