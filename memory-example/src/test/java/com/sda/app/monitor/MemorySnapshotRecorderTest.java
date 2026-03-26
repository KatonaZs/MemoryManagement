package com.sda.app.monitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemorySnapshotRecorderTest {

    @Mock
    private Metric<Long> metric;
    private MemorySnapshotRecorder underTest;

    @BeforeEach
    void setup() {
        underTest = new MemorySnapshotRecorder(metric);
    }

    @Test
    public void recordShouldRegisterANew() {
        // GIVEN
        final Long expectedRecordValue= 2L;
        when(metric.retrieveCalculatedMetricValue()).thenReturn(expectedRecordValue);

        // WHEN
        underTest.record();

        // THEN
        assertEquals(expectedRecordValue, underTest.retrieveRecords().getFirst());
    }

    @Test
    public void retrieveRecordsShouldReturnRecordedValue() {
        // GIVEN
        final Long expectedRecordValue= 2L;
        when(metric.retrieveCalculatedMetricValue()).thenReturn(expectedRecordValue);
        underTest.record();

        // WHEN
        final Long result = underTest.retrieveRecords().getFirst();

        // THEN
        assertEquals(expectedRecordValue, result);
    }
}
