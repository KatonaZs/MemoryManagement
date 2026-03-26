package com.sda.app.monitor;

import java.util.ArrayList;
import java.util.List;

public class MemorySnapshotRecorder implements Recorder<Long> {
    private final static List<Long> RECORDS = new ArrayList<>();
    private final Metric<Long> metric;

    public MemorySnapshotRecorder(final Metric<Long> metric) {
        this.metric = metric;
    }

    @Override
    public synchronized void record() {
        RECORDS.add(metric.retrieveCalculatedMetricValue());
    }

    @Override
    public List<Long> retrieveRecords() {
        return RECORDS;
    }
}
