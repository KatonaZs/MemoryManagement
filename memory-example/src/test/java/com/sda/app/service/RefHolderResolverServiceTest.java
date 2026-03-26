package com.sda.app.service;

import com.sda.app.util.ReferenceType;
import org.junit.jupiter.api.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class RefHolderResolverServiceTest {

    private final RefHolderResolverService service = new RefHolderResolverService();

    @Test
    void resolveRefHolderShouldReturnSoftReferenceType() {
        // GIVEN
        Supplier<Integer> supplier = () -> 1;

        // WHEN
        Object result = service.resolveRefHolder(supplier, ReferenceType.SOFT);

        // THEN
        assertInstanceOf(SoftReference.class, result);
        SoftReference<?> softRef = (SoftReference<?>) result;
        assertEquals(supplier, softRef.get());
    }

    @Test
    void resolveRefHolderShouldReturnWeakReferenceType() {
        // GIVEN
        Supplier<Integer> supplier = () -> 2;

        // WHEN
        Object result = service.resolveRefHolder(supplier, ReferenceType.WEAK);

        // THEN
        assertInstanceOf(WeakReference.class, result);
        WeakReference<?> softRef = (WeakReference<?>) result;
        assertEquals(supplier, softRef.get());
    }



    @Test
    void resolveRefHolderShouldReturnWeakReferenceTypeEvenIfTheSupplierNull() {
        // GIVEN
        Supplier<Integer> supplier = null;

        // WHEN
        Object result = service.resolveRefHolder(supplier, ReferenceType.WEAK);

        // THEN
        assertInstanceOf(WeakReference.class, result);
        WeakReference<?> softRef = (WeakReference<?>) result;
        assertEquals(supplier, softRef.get());
    }
}
