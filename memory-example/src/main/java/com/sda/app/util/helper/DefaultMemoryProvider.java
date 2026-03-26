package com.sda.app.util.helper;

/*
* Main purpose of this class to provide a Wrapper for Runtime calls for making the testing easier
*/
public class DefaultMemoryProvider implements MemoryProvider {

    @Override
    public long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    @Override
    public long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }
}