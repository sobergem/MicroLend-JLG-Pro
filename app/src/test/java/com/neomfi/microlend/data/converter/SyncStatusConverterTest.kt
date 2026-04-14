package com.neomfi.microlend.data.converter

import com.neomfi.microlend.data.local.entity.SyncStatus
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SyncStatusConverterTest {

    private lateinit var converter: SyncStatusConverter

    @Before
    fun setUp() {
        converter = SyncStatusConverter()
    }

    // --- fromSyncStatus ---

    @Test
    fun fromSyncStatus_pending_returnsCorrectString() {
        assertEquals("PENDING", converter.fromSyncStatus(SyncStatus.PENDING))
    }

    @Test
    fun fromSyncStatus_syncing_returnsCorrectString() {
        assertEquals("SYNCING", converter.fromSyncStatus(SyncStatus.SYNCING))
    }

    @Test
    fun fromSyncStatus_synced_returnsCorrectString() {
        assertEquals("SYNCED", converter.fromSyncStatus(SyncStatus.SYNCED))
    }

    @Test
    fun fromSyncStatus_failed_returnsCorrectString() {
        assertEquals("FAILED", converter.fromSyncStatus(SyncStatus.FAILED))
    }

    // --- toSyncStatus ---

    @Test
    fun toSyncStatus_pendingString_returnsPendingEnum() {
        assertEquals(SyncStatus.PENDING, converter.toSyncStatus("PENDING"))
    }

    @Test
    fun toSyncStatus_syncingString_returnsSyncingEnum() {
        assertEquals(SyncStatus.SYNCING, converter.toSyncStatus("SYNCING"))
    }

    @Test
    fun toSyncStatus_syncedString_returnsSyncedEnum() {
        assertEquals(SyncStatus.SYNCED, converter.toSyncStatus("SYNCED"))
    }

    @Test
    fun toSyncStatus_failedString_returnsFailedEnum() {
        assertEquals(SyncStatus.FAILED, converter.toSyncStatus("FAILED"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun toSyncStatus_unknownString_throwsIllegalArgumentException() {
        converter.toSyncStatus("UNKNOWN_VALUE")
    }

    @Test(expected = IllegalArgumentException::class)
    fun toSyncStatus_emptyString_throwsIllegalArgumentException() {
        converter.toSyncStatus("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun toSyncStatus_lowercaseString_throwsIllegalArgumentException() {
        // SyncStatus.valueOf is case-sensitive
        converter.toSyncStatus("pending")
    }

    // --- Round-trip tests ---

    @Test
    fun roundTrip_pending_preservesValue() {
        val original = SyncStatus.PENDING
        val converted = converter.fromSyncStatus(original)
        val restored = converter.toSyncStatus(converted)
        assertEquals(original, restored)
    }

    @Test
    fun roundTrip_synced_preservesValue() {
        val original = SyncStatus.SYNCED
        val converted = converter.fromSyncStatus(original)
        val restored = converter.toSyncStatus(converted)
        assertEquals(original, restored)
    }

    @Test
    fun roundTrip_allValues_preserveValues() {
        SyncStatus.entries.forEach { status ->
            val converted = converter.fromSyncStatus(status)
            val restored = converter.toSyncStatus(converted)
            assertEquals(status, restored)
        }
    }
}