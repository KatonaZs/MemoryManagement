package com.sda.app.service;

import com.sda.app.util.ReferenceType;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;

public class RefHolderResolverService {

    public Object resolveRefHolder(final Supplier<Integer> supplier, final ReferenceType referenceType) {
        return referenceType == ReferenceType.SOFT ? new SoftReference<>(supplier) : new WeakReference<>(supplier);
    }

}
