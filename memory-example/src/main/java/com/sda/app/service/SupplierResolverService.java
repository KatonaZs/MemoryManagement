package com.sda.app.service;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;

public class SupplierResolverService {

    @SuppressWarnings("unchecked")
    public Supplier<Integer> dereferenceSupplier(final Object refHolder) {
        Supplier<Integer> supplier = null;

        if (refHolder instanceof SoftReference) {
            supplier = (Supplier<Integer>) ((SoftReference<?>) refHolder).get();
        } else if (refHolder instanceof WeakReference) {
            supplier = (Supplier<Integer>) ((WeakReference<?>) refHolder).get();
        }

        return supplier;
    }
}
