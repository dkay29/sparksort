package com.dkay29.spark.sort.unsafe;

import com.dkay29.spark.sort.offheap.OffSortingMemory;
import com.dkay29.spark.sort.offheap.OffheapQuickSort;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OffheapQuickSortTest {

    @Test
    public void testSorting() {
        Random random = new Random(12345);
        OffSortingMemory osm = new OffSortingMemory(1000L, 100);
        for (long i = 0; i < osm.getEntryCount(); i++) {
            byte[] val = new byte[osm.getEntrySize()];
            for (int j = 0; j < val.length; j++) {
                val[j] = (byte) (0x0000f & random.nextInt());
            }
            osm.set(i, val);
        }
        OffheapQuickSort qs = new OffheapQuickSort(osm);
        qs.sort();
        byte[] prior = new byte[osm.getEntrySize()];
        System.out.println("---------------------");
        for (int i = 0; i < prior.length; prior[i++] = -128) ;
        for (long i = 0; i < osm.getEntryCount(); i++) {
            byte[] v = osm.get(i);
            int diff = OffheapQuickSort.compare(prior, v);
            assertTrue(diff > -1);
            prior = v;
        }
        osm.freeMemory();
    }

    String toHexString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    @Test
    public void testByteArrayCompare() {
        byte[][] vals = {{0, 0, 0}, {1, 0, 0}};
        assertEquals(1, OffheapQuickSort.compare(vals[1], vals[0]));
        assertEquals(0, OffheapQuickSort.compare(vals[1], vals[1]));
        assertEquals(-1, OffheapQuickSort.compare(vals[0], vals[1]));
    }
}
