package com.sda.app.monitor;

import java.util.ArrayList;
import java.util.List;

public class MemorySnapshotRecorder implements Recorder<Long> {
    private final List<Long> records = new ArrayList<>();

    @Override
    public void record() {
        records.add(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    @Override
    public List<Long> retrieveRecords() {
        return records;
    }
}
