package com.sda.app;

import com.sda.app.monitor.MemorySnapshotRecorder;
import com.sda.app.monitor.Recorder;
import com.sda.app.monitor.display.Displayer;
import com.sda.app.monitor.display.SynchronizedHistogramDisplayer;
import com.sda.app.service.SupplierResolverService;
import com.sda.app.service.fibonacci.FibCompletableService;
import com.sda.app.util.ReferenceType;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {
        final int n = 28; // tune as desired
        final SupplierResolverService  supplierResolverService = new SupplierResolverService();

        // Create virtual-thread executor and ensure it is closed to avoid resource leaks
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())) {
            // SoftReference run
            final Recorder<Long> softRecorder = new MemorySnapshotRecorder();
            final Displayer softDisplayer = new SynchronizedHistogramDisplayer(softRecorder, System.out);
            FibCompletableService softFib = new FibCompletableService(executor, softRecorder,
                    ReferenceType.SOFT, supplierResolverService);

            System.out.println("Starting SoftReference Fibonacci run (CompletableFuture + virtual threads)...");
            System.gc(); // hint GC to clear soft refs under memory pressure
            int softResult = softFib.compute(n);
            System.out.println("FibonacciSoft(" + n + ") = " + softResult);
            softDisplayer.display("SoftReference");

            // WeakReference run
            final Recorder<Long> weakRecorder = new MemorySnapshotRecorder();
            final Displayer weakDisplayer = new SynchronizedHistogramDisplayer(weakRecorder, System.out);
            FibCompletableService weakFib = new FibCompletableService(executor, weakRecorder,
                    ReferenceType.WEAK, supplierResolverService);

            System.out.println("Starting WeakReference Fibonacci run (CompletableFuture + virtual threads)...");
            System.gc(); // encourage GC to clear weak refs
            int weakResult = weakFib.compute(n);
            System.out.println("FibonacciWeak(" + n + ") = " + weakResult);
            weakDisplayer.display("WeakReference");

        } // executor closed here
    }
}