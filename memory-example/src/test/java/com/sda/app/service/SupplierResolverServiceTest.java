package com.sda.app.service;


import org.junit.jupiter.api.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SupplierResolverServiceTest {

    private final SupplierResolverService underTest = new SupplierResolverService();

    @Test
    public void dereferenceSupplierShouldReturnNullIfTheRefHolderIsNotSupported() {
        // GIVEN
        final Object dummyPhantomRefHolder = new PhantomReference<>(null, null);

        // WHEN
        final Supplier<Integer> result = underTest.dereferenceSupplier(dummyPhantomRefHolder);

        // THEN
        assertNull(result);
    }

    @Test
    public void dereferenceSupplierShouldReturnNullIfTheRefHolderIsNull() {
        // GIVEN
        final Object nullRefHolder = null;

        // WHEN
        final Supplier<Integer> result = underTest.dereferenceSupplier(nullRefHolder);

        // THEN
        assertNull(result);
    }

    @Test
    public void dereferenceSupplierShouldReturnSupplierIfTheReferenceIsSoft() {
        // GIVEN
        final Supplier<Integer> supplier = () -> 1;
        final Object softReference = new SoftReference<>(supplier);

        // WHEN
        final Supplier<Integer> result = underTest.dereferenceSupplier(softReference);

        // THEN
        assertEquals(1, result.get());
    }

    @Test
    public void dereferenceSupplierShouldReturnSupplierIfTheReferenceIsWeak() {
        // GIVEN
        final Supplier<Integer> supplier = () -> 2;
        final Object weakReference = new WeakReference<>(supplier);

        // WHEN
        final Supplier<Integer> result = underTest.dereferenceSupplier(weakReference);

        // THEN
        assertEquals(2, result.get());
    }
}
