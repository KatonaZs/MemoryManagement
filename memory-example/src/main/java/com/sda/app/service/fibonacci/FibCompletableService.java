package com.sda.app.service.fibonacci;

import com.sda.app.monitor.Recorder;
import com.sda.app.service.RefHolderResolverService;
import com.sda.app.service.SupplierResolverService;
import com.sda.app.util.ReferenceType;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class FibCompletableService implements FibComputer {

    // Use a threshold to avoid excessive trivial tasks
    private static final int SPAWN_THRESHOLD = 10;

    private final Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final Recorder<Long> recorder;
    private final ReferenceType referenceType;
    private final SupplierResolverService supplierResolverService;
    private final RefHolderResolverService refHolderResolverService;

    public FibCompletableService(final ExecutorService executor, final Recorder<Long> recorder,
                                 final ReferenceType type, final SupplierResolverService supplierResolverService,
                                 final RefHolderResolverService refHolderResolverService) {
        this.executor = executor;
        this.recorder = recorder;
        this.referenceType = type;
        this.supplierResolverService = supplierResolverService;
        this.refHolderResolverService = refHolderResolverService;
    }

    @Override
    public int compute(int n) {
        recorder.record();
        if (n <= 1) return n;

        // Check if the map is already containing the requested n-th fib number
        Integer currentNumber = concurrentHashMap.get(n);
        if (currentNumber != null) {
            return currentNumber;
        }

        // For small n, avoid async spawn to reduce overhead
        if (n <= SPAWN_THRESHOLD) {
            return compute(n - 1) + compute(n - 2);
        }

        // Create a Supplier that computes n-1
        Supplier<Integer> supplier = () -> compute(n - 1);

        // Wrap supplier in a SoftReference or WeakReference (for demonstration)
        Object refHolder = refHolderResolverService.resolveRefHolder(supplier, referenceType);


        // Safely dereference or recreate supplier if GC cleared it
        Supplier<Integer> safeSupplier = Optional.ofNullable(supplierResolverService.dereferenceSupplier(refHolder))
                .orElseGet(() -> (Supplier<Integer>) (() -> compute(n - 1)));

        // Start async subtask on a virtual thread
        CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(safeSupplier, executor);

        // Compute n-2 inline (no extra thread)
        int f2 = compute(n - 2);
        // Join result (cheap if blocked on virtual thread)
        int f1 = cf.join();
        int result = f1 + f2;

        Integer newValue = concurrentHashMap.putIfAbsent(n, result);
        // If the value was inserted means that it is a new value, insert happens if putIfAbsent returns null
        if(newValue == null) {
            System.out.println(result);
        }

        return result;
    }
}
