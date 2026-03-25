package com.sda.app.monitor;

import java.util.List;

public interface Recorder<T> {
    void record(final Long value);
    List<T> retrieveRecords();
}
