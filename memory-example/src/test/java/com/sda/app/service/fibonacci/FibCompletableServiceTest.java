package com.sda.app.service.fibonacci;

import com.sda.app.monitor.Recorder;
import com.sda.app.service.RefHolderResolverService;
import com.sda.app.service.SupplierResolverService;
import com.sda.app.util.ReferenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FibCompletableServiceTest {

    @Mock
    private Recorder<Long> recorder;
    @Mock
    private SupplierResolverService supplierResolverService;
    @Mock
    private RefHolderResolverService refHolderResolverService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private FibCompletableService underTest;

    @BeforeEach
    void setup() {
        underTest = new FibCompletableService(executor, recorder, ReferenceType.SOFT,
                supplierResolverService, refHolderResolverService);
    }

    @Test
    public void computeShouldReturnTheParamIfLessThanOne() {
        // GIVEN
        int expected = 0;

        // WHEN
        int result = underTest.compute(expected);

        // THEN
        assertEquals(expected, result);
        verify(recorder).record();
    }

    @Test
    public void computeShouldReturnTheParamIfEqualsOne() {
        // GIVEN
        int expected = 1;

        // WHEN
        int result = underTest.compute(expected);

        // THEN
        assertEquals(expected, result);
        verify(recorder).record();
    }

    @Test
    public void computeShouldAvoidAsyncSpawnIfParamLessThanThreshold() {
        // GIVEN
        int number = 3;
        int expectedResult = 2;

        // WHEN
        int result = underTest.compute(number);

        // THEN
        assertEquals(expectedResult, result);
        verify(recorder, atLeastOnce()).record();
    }

    @Test
    public void computeShouldAvoidAsyncSpawnIfParamEqualsThreshold() {
        // GIVEN
        int number = 10;
        int expectedResult = 55;

        // WHEN
        int result = underTest.compute(number);

        // THEN
        assertEquals(expectedResult, result);
        verify(recorder, atLeastOnce()).record();
    }

    @Test
    public void computeShouldUseCompletableFutureIfParamMoreThanThreshold() {
        // GIVEN
        int number = 11;
        int expectedResult = 89;
        Supplier<Integer> supplier = () -> 55;
        Object refHolder = new Object();
        when(refHolderResolverService.resolveRefHolder(any(), eq(ReferenceType.SOFT))).thenReturn(refHolder);
        when(supplierResolverService.dereferenceSupplier(refHolder)).thenReturn(supplier);

        // WHEN
        int result = underTest.compute(number);

        // THEN
        assertEquals(expectedResult, result);
        verify(recorder, atLeastOnce()).record();
        verify(refHolderResolverService, atLeastOnce()).resolveRefHolder(any(), eq(ReferenceType.SOFT));
        verify(supplierResolverService, atLeastOnce()).dereferenceSupplier(refHolder);
    }
}
