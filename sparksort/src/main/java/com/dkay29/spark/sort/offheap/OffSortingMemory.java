package com.dkay29.spark.sort.offheap;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public class OffSortingMemory {
    private final static int BYTE = 1;
    private final long entryCount;
    private int entrySize;
    private long address;
    private long size;
    private AtomicBoolean memWasFreed = new AtomicBoolean(false);

    public OffSortingMemory(long entryCount, int entrySize) {
        this.entryCount = entryCount;
        this.entrySize = entrySize;
        try {
            address = getUnsafe().allocateMemory(size = (entryCount * entrySize * BYTE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Unsafe getUnsafe() throws IllegalAccessException, NoSuchFieldException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        return (Unsafe) f.get(null);
    }

    private long checkArrayBounds(long i) {
        if (i < 0 || i >= entryCount) {
            throw new IndexOutOfBoundsException("" + i+" > "+entryCount);
        }
        return i;
    }

    public void set(long i, byte[] bytes) {
        if (bytes.length != entrySize) {
            throw new RuntimeException("bytes is not of length " + entrySize + " : " + bytes.length);
        }
        checkArrayBounds(i);
        for (int j = 0; j < bytes.length; j++) {
            try {
                checkMem();
                getUnsafe().putByte(address + i * entrySize * BYTE + j, bytes[j]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkMem() {
        if (memWasFreed.get()) {
            throw new RuntimeException("accessing unsafe after memory was freed");
        }

    }
    public byte[] get(long i) {
        checkArrayBounds(i);
        byte[] r = new byte[entrySize];
        try {
            checkMem();
            for (int j = 0; j < r.length; j++) {
                r[j] = getUnsafe().getByte(address + i * entrySize * BYTE + j);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return r;
    }

    public long size() {
        return size;
    }

    public long getEntryCount() {
        return entryCount;
    }

    public int getEntrySize() {
        return entrySize;
    }

    public void swapElements(long i, long j) {
        if (i == j) {
            return;
        }
        byte[] tmp = this.get(j);
        try {
            checkMem();
            getUnsafe().copyMemory(address + i * entrySize, address + j * entrySize, entrySize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.set(i, tmp);
    }

    public void freeMemory() {
        try {
            memWasFreed.set(true);
            getUnsafe().freeMemory(address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
