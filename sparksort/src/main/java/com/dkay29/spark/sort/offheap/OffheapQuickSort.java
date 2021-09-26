package com.dkay29.spark.sort.offheap;

public class OffheapQuickSort {
    private final OffSortingMemory osm;

    public OffheapQuickSort(OffSortingMemory osm) {
        this.osm = osm;
    }
    public void sort() {
        quickSort(0,osm.getEntryCount()-1);
    }
    private void quickSort(long begin, long end) {
        if (begin<end) {
            long partitionIndex = partition(begin, end);
            quickSort(begin, partitionIndex-1L);
            quickSort(partitionIndex+1L, end);
        }
    }
    private long partition( long begin, long end) {
        byte[] pivot = osm.get(end);
        long i = (begin-1);
        for (long j= begin; j< end;j++) {
            byte[] jele=osm.get(j);
            int compCode=compare(pivot,jele);
            if (compCode<=0) {
                i++;
                osm.swapElements(i,j);
            }
        }
        osm.swapElements(i+1,end);
        return i+1;
    }
    public static int compare(byte[] a,byte[]b) {
        for (int i=0;i<a.length;i++) {
            if (b[i]<a[i]) {
                return -1;
            } else if (b[i]>a[i]) {
                return 1;
            }
        }
        return 0;
    }
}
