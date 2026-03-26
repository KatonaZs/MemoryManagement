package com.sda.app.monitor.display;

import com.sda.app.monitor.Recorder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SynchronizedHistogramDisplayerTest {

    @Mock
    private Recorder<Long> recorder;
    @Mock
    private PrintStream printStream;
    private SynchronizedHistogramDisplayer underTest;

    @BeforeEach
    void setup() {
        underTest = new SynchronizedHistogramDisplayer(recorder, printStream);
    }

    @Test
    void displayShouldPrintNoDataWhenRecordsEmpty() {
        // GIVEN
        when(recorder.retrieveRecords()).thenReturn(Collections.emptyList());

        // WHEN
        underTest.display("TestLabel");

        // THEN
        final InOrder inOrder = inOrder(printStream);
        inOrder.verify(printStream).println(anyString());
        verify(recorder).retrieveRecords();
        verifyNoMoreInteractions(printStream);
    }

    @Test
    void displayShouldPrintHistogramsAndClearRecordsWhenRecordsWasProcessed() {
        // GIVEN
        final List<Long> records = new ArrayList<>(List.of(1L));
        when(recorder.retrieveRecords()).thenReturn(records);

        // WHEN
        underTest.display("MyLabel");

        // THEN
        verify(printStream).printf(anyString(), anyDouble(), anyString());
        verify(printStream).println();
        verify(recorder).retrieveRecords();
        verifyNoMoreInteractions(printStream);
        assertTrue(records.isEmpty());
    }

    @Test
    void displayShouldPrintHistogramsClearRecordsAndSkipIfNullWhenRecordsWasProcessed() {
        // GIVEN
        final List<Long> records = new ArrayList<>(Arrays.asList(1L, null));
        when(recorder.retrieveRecords()).thenReturn(records);

        // WHEN
        underTest.display("MyLabel");

        // THEN
        verify(printStream).printf(anyString(), anyDouble(), anyString());
        verify(printStream).println();
        verify(recorder).retrieveRecords();
        verifyNoMoreInteractions(printStream);
        assertTrue(records.isEmpty());
    }
}
