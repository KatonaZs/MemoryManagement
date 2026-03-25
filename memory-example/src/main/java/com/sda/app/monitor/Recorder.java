package com.sda.app.monitor;

import java.util.List;

public interface Recorder<T> {
    void record();
    List<T> retrieveRecords();
}
