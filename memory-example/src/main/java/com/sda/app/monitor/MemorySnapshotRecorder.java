package com.sda.app.monitor;

import java.util.ArrayList;
import java.util.List;

public class MemorySnapshotRecorder implements Recorder<Long> {
    private final List<Long> records = new ArrayList<>();

    @Override
    public synchronized void record(final Long value) {
        records.add(value);
    }

    @Override
    public List<Long> retrieveRecords() {
        return records;
    }
}
