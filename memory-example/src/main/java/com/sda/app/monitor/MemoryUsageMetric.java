package com.sda.app.monitor;

public class MemoryUsageMetric implements Metric<Long> {

    private final Runtime runtime;

    public MemoryUsageMetric(final Runtime runtime) {
        this.runtime = runtime;
    }

    @Override
    public Long retrieveCalculatedMetricValue() {
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
