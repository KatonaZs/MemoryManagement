package com.sda.app.monitor.display;

import com.sda.app.monitor.Recorder;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

public class SynchronizedHistogramDisplayer implements Displayer {

    private static final int MAX_BAR_LENGTH = 60;
    private final Recorder<Long> recorder;
    private final PrintStream printStream;

    public SynchronizedHistogramDisplayer(final Recorder<Long> recorder, final PrintStream printStream) {
        this.recorder = recorder;
        this.printStream = printStream;
    }

    @Override
    public synchronized void display(final String label) {
        System.out.println(label + " Memory Usage Histogram:");
        List<Long> records = recorder.retrieveRecords();
        if (records.isEmpty()) {
            printStream.println("(no data)\n");
            return;
        }

        long max = records.stream().filter(Objects::nonNull).mapToLong(Long::longValue).max().orElse(1L);

        for (Long val : records) {
            if (val == null) continue;
            int barLength = (int) ((val * MAX_BAR_LENGTH) / max);
            String bar = "=".repeat(Math.max(0, barLength));
            printStream.printf("%6.2f MB |%s%n", val / 1024.0 / 1024.0, bar);
        }
        records.clear();
        printStream.println();
    }
}
