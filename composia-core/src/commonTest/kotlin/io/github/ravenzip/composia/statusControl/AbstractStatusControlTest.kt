package io.github.ravenzip.composia.statusControl

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import io.github.ravenzip.composia.control.statusControl.AbstractStatusControl
import io.github.ravenzip.composia.state.ControlState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AbstractStatusControlTest {
    private class BaseStatusControl(disabled: Boolean = false, scope: CoroutineScope) :
        AbstractStatusControl(disabled, scope)

    @Test
    fun `initial state is Valid when not disabled`() = runTest {
        val control = BaseStatusControl(scope = backgroundScope)

        assertEquals(ControlState.Valid, control.status)
        assertTrue(control.isEnabled)
        assertFalse(control.isDisabled)
    }

    @Test
    fun `initial state is Disabled when disabled`() = runTest {
        val control = BaseStatusControl(disabled = true, scope = backgroundScope)

        assertEquals(ControlState.Disabled, control.status)
        assertTrue(control.isDisabled)
        assertFalse(control.isEnabled)
    }

    @Test
    fun `state changed to Disabled`() = runTest {
        val control = BaseStatusControl(scope = backgroundScope)
        control.disable()

        assertEquals(ControlState.Disabled, control.status)
        assertTrue(control.isDisabled)
        assertFalse(control.isEnabled)
    }

    @Test
    fun `state changed to Valid`() = runTest {
        val control = BaseStatusControl(disabled = true, scope = backgroundScope)
        control.enable()

        assertEquals(ControlState.Valid, control.status)
        assertTrue(control.isEnabled)
        assertFalse(control.isDisabled)
    }

    @Test
    fun `reset restores initial state`() = runTest {
        val enabledControl = BaseStatusControl(scope = backgroundScope)

        enabledControl.disable()
        assertEquals(ControlState.Disabled, enabledControl.status)
        assertTrue(enabledControl.isDisabled)
        assertFalse(enabledControl.isEnabled)

        enabledControl.reset()
        assertEquals(ControlState.Valid, enabledControl.status)
        assertTrue(enabledControl.isEnabled)
        assertFalse(enabledControl.isDisabled)

        val disabledControl = BaseStatusControl(disabled = true, scope = backgroundScope)

        disabledControl.enable()
        assertEquals(ControlState.Valid, disabledControl.status)
        assertTrue(disabledControl.isEnabled)
        assertFalse(disabledControl.isDisabled)

        disabledControl.reset()
        assertEquals(ControlState.Disabled, disabledControl.status)
        assertTrue(disabledControl.isDisabled)
        assertFalse(disabledControl.isEnabled)
    }

    @Test
    fun `statusFlow emits changes`() = runTest {
        turbineScope {
            val control = BaseStatusControl(scope = backgroundScope)

            control.statusFlow.test {
                assertEquals(ControlState.Valid, awaitItem())

                control.disable()
                assertEquals(ControlState.Disabled, awaitItem())

                control.enable()
                assertEquals(ControlState.Valid, awaitItem())
            }
        }
    }

    @Test
    fun `isDisabledFlow emits changes`() = runTest {
        turbineScope {
            val control = BaseStatusControl(scope = backgroundScope)

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
            val control = BaseStatusControl(disabled = true, scope = backgroundScope)

            control.isEnabledFlow.test {
                assertFalse(awaitItem())

                control.enable()
                assertTrue(awaitItem())

                control.disable()
                assertFalse(awaitItem())
            }
        }
    }

    @Test
    fun `isEnabled has correct value by isEnabledFlow`() = runTest {
        turbineScope {
            val control = BaseStatusControl(disabled = true, scope = backgroundScope)

            assertFalse(control.getIsEnabled())

            control.enable()
            println(control.status)

            assertTrue(control.getIsEnabled())
        }
    }
}
