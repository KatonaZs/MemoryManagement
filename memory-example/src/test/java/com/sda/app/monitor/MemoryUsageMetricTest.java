package com.sda.app.monitor;

import com.sda.app.util.helper.MemoryProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemoryUsageMetricTest {

    @Mock
    private MemoryProvider memoryProvider;
    private MemoryUsageMetric underTest;

    @BeforeEach
    void setup() {
        underTest = new MemoryUsageMetric(memoryProvider);
    }

    @Test
    public void retrieveCalculatedMetricValueShouldCalculateMemoryUsage() {
        // GIVEN
        final Long totalMemory = 3L;
        final Long freeMemory = 2L;
        final Long expectedUsedMemory = 1L;

        when(memoryProvider.totalMemory()).thenReturn(totalMemory);
        when(memoryProvider.freeMemory()).thenReturn(freeMemory);

        // THEN
        final Long result = underTest.retrieveCalculatedMetricValue();

        // THEN
        assertEquals(expectedUsedMemory, result);
    }
}
