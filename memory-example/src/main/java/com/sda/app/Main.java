package com.sda.app;

import com.sda.app.monitor.MemorySnapshotRecorder;
import com.sda.app.monitor.MemoryUsageMetric;
import com.sda.app.monitor.Recorder;
import com.sda.app.monitor.display.Displayer;
import com.sda.app.monitor.display.SynchronizedHistogramDisplayer;
import com.sda.app.service.RefHolderResolverService;
import com.sda.app.service.SupplierResolverService;
import com.sda.app.service.fibonacci.FibCompletableService;
import com.sda.app.util.ReferenceType;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class Main {

    public static void main(String[] args) {
        final int n = 28; // tune as desired
        final SupplierResolverService  supplierResolverService = new SupplierResolverService();
        final RefHolderResolverService refHolderResolverService = new RefHolderResolverService();

        // Create virtual-thread executor and ensure it is closed to avoid resource leaks
        try (ExecutorService executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())) {
            // SoftReference run
            final MemoryUsageMetric memoryUsageMetric = new MemoryUsageMetric(Runtime.getRuntime());
            final Recorder<Long> softRefRecorder = new MemorySnapshotRecorder(memoryUsageMetric);
            final Displayer softDisplayer = new SynchronizedHistogramDisplayer(softRefRecorder, System.out);
            FibCompletableService softFib = new FibCompletableService(executor, softRefRecorder,
                    ReferenceType.SOFT, supplierResolverService, refHolderResolverService);

            System.out.println("Starting SoftReference Fibonacci run (CompletableFuture + virtual threads)...");
            System.gc(); // hint GC to clear soft refs under memory pressure
            int softResult = softFib.compute(n);
            System.out.println("FibonacciSoft(" + n + ") = " + softResult);
            softDisplayer.display("SoftReference");

            // WeakReference run
            final MemoryUsageMetric memory2UsageMetric = new MemoryUsageMetric(Runtime.getRuntime());
            final Recorder<Long> weakRefRecorder = new MemorySnapshotRecorder(memory2UsageMetric);
            final Displayer weakDisplayer = new SynchronizedHistogramDisplayer(weakRefRecorder, System.out);
            FibCompletableService weakFib = new FibCompletableService(executor, weakRefRecorder,
                    ReferenceType.WEAK, supplierResolverService, refHolderResolverService);

            System.out.println("Starting WeakReference Fibonacci run (CompletableFuture + virtual threads)...");
            System.gc(); // encourage GC to clear weak refs
            int weakResult = weakFib.compute(n);
            System.out.println("FibonacciWeak(" + n + ") = " + weakResult);
            weakDisplayer.display("WeakReference");

        } // executor closed here
    }
}