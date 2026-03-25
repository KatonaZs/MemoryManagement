package com.sda.app.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MemorySnapshotRecorderTest {

    private final MemorySnapshotRecorder underTest = new MemorySnapshotRecorder();

    @Test
    public void recordShouldRegisterANew() {
        // GIVEN
        final Long expectedRecordValue= 2L;

        // WHEN
        underTest.record(expectedRecordValue);

        // THEN
        assertEquals(expectedRecordValue, underTest.retrieveRecords().getFirst());
    }

    @Test
    public void retrieveRecordsShouldReturnRecordedValue() {
        // GIVEN
        final Long expectedRecordValue= 2L;
        underTest.record(expectedRecordValue);

        // WHEN
        final Long result = underTest.retrieveRecords().getFirst();

        // THEN
        assertEquals(expectedRecordValue, result);
    }
}
