package com.sda.app.monitor;

public interface Metric<T> {

    T retrieveCalculatedMetricValue();
}
