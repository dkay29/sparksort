package com.dkay29.spark.sort.offheap;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class OffSortingMemory {
    private final static int BYTE = 1;
    private final long entryCount;
    private int entrySize;
    private long address;
    private long size;

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
            throw new IndexOutOfBoundsException("" + i);
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
                getUnsafe().putByte(address + i * entrySize * BYTE + j, bytes[j]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public byte[] get(long i) {
        byte[] r = new byte[entrySize];
        try {
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

    public void swapElements(int i, int j) {
        byte[] tmp = this.get(j);
        try {
            getUnsafe().copyMemory(address + i * entrySize, address + j * entrySize, entrySize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.set(i, tmp);
    }

    public void freeMemory() throws NoSuchFieldException, IllegalAccessException {
        getUnsafe().freeMemory(address);
    }
}
