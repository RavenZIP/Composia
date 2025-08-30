package io.github.ravenzip.composia.statusControl

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import io.github.ravenzip.composia.control.shared.ControlStatus
import io.github.ravenzip.composia.control.statusControl.StatusControl
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class StatusControlTest {
    @Test
    fun `initial snapshot status is Valid when not disabled`() = runTest {
        val control = StatusControl(coroutineScope = backgroundScope)

        assertEquals(ControlStatus.Valid, control.snapshot.status)
        assertTrue(control.snapshot.isEnabled)
        assertFalse(control.snapshot.isDisabled)
    }

    @Test
    fun `initial snapshot status is Disabled when disabled`() = runTest {
        val control = StatusControl(disabled = true, coroutineScope = backgroundScope)

        assertEquals(ControlStatus.Disabled, control.snapshot.status)
        assertFalse(control.snapshot.isEnabled)
        assertTrue(control.snapshot.isDisabled)
    }

    @Test
    fun `snapshotFlow emit changes`() = runTest {
        turbineScope {
            val control = StatusControl(coroutineScope = backgroundScope)

            control.snapshotFlow.test {
                val initialSnapshot = awaitItem()

                assertEquals(ControlStatus.Valid, initialSnapshot.status)
                assertTrue(initialSnapshot.isEnabled)
                assertFalse(initialSnapshot.isDisabled)

                control.disable()

                val disabledSnapshot = awaitItem()

                assertEquals(ControlStatus.Disabled, disabledSnapshot.status)
                assertTrue(disabledSnapshot.isDisabled)
                assertFalse(disabledSnapshot.isEnabled)
            }
        }
    }
}
