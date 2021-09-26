package com.dkay29.spark.sort.unsafe;

import com.dkay29.spark.sort.offheap.OffSortingMemory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OffSortingMemoryTest {
    @Test
    public void simpleElementPutGetCheck()
    {
        byte[] one = strToPaddedBytes("One",10);
        assertEquals("One",bytesToString(one));
        OffSortingMemory offSortingMemory = new OffSortingMemory(10,10);
        offSortingMemory.set(0,strToPaddedBytes("One",10));
        offSortingMemory.set(1,strToPaddedBytes("Two",10));
        offSortingMemory.set(2,strToPaddedBytes("Three",10));
        offSortingMemory.set(3,strToPaddedBytes("Four",10));
        offSortingMemory.set(4,strToPaddedBytes("Five",10));
        offSortingMemory.set(5,strToPaddedBytes("Six",10));
        offSortingMemory.set(6,strToPaddedBytes("Seven",10));
        offSortingMemory.set(7,strToPaddedBytes("Eight",10));
        offSortingMemory.set(8,strToPaddedBytes("Nine",10));
        offSortingMemory.set(9,strToPaddedBytes("Ten",10));
        assertEquals("One",bytesToString(offSortingMemory.get(0)));
        assertEquals("Three",bytesToString(offSortingMemory.get(2)));
        assertEquals(100,offSortingMemory.size());
        offSortingMemory.swapElements(0,9);
        assertEquals("One",bytesToString(offSortingMemory.get(9)));
        assertEquals("Ten",bytesToString(offSortingMemory.get(0)));
        assertEquals("bytes is not of length 10 : 1",getExceptionRunning(()->offSortingMemory.set(10,"0".getBytes())).getMessage());
        assertEquals("11",getExceptionRunning(()->offSortingMemory.set(11,strToPaddedBytes("x",10))).getMessage());
    }

    private Exception getExceptionRunning(Runnable r) {
        Exception ex=null;
        try {
            r.run();
        } catch (Exception e) {
            ex=e;
        }
        return ex;
    }

    private byte[] strToPaddedBytes(String s,int size) {
        byte[] r = new byte[size];
        byte[] t = s.getBytes();
        if (t.length>size) {
            throw new IndexOutOfBoundsException(""+t.length);
        }
        System.arraycopy(t,0,r,0,t.length);
        return r;
    }
    private String bytesToString(byte[] arr) {
        int i=0;
        for(;arr[i]!=0;i++);
        byte[] t =new byte[i];
        System.arraycopy(arr,0,t,0,i);
        return new String(t);
    }
}
