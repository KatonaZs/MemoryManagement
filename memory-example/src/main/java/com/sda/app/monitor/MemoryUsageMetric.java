package com.sda.app.monitor;

import com.sda.app.util.helper.MemoryProvider;

public class MemoryUsageMetric implements Metric<Long> {

    private final MemoryProvider memoryProvider;

    public MemoryUsageMetric(final MemoryProvider memoryProvider) {
        this.memoryProvider = memoryProvider;
    }

    @Override
    public Long retrieveCalculatedMetricValue() {
        return memoryProvider.totalMemory() - memoryProvider.freeMemory();
    }
}
