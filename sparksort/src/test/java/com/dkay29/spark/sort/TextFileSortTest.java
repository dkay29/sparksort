package com.dkay29.spark.sort;

import org.junit.Before;

import java.io.*;

public class TextFileSortTest {

    File unsortedFile=null;

    @Before
    public void writeUnsortredFile() throws IOException {
        File f = File.createTempFile("unsorted","unsorted");
        try (PrintWriter pr = new PrintWriter(new FileWriter(f))) {
            pr.println("one 1   aaaa");
            pr.println("two 2.3 aaa");
            pr.println("two 2.3 a  ds a sa as fs dg asdg   gfad ga fd gfda  ag df ag a dfg ad gf ad ga dg");
            pr.println("two 0.23 a");
            pr.println("two 0.0000000000000023 a");
            pr.println("two 10000000000001 a");
            pr.println("two 10000000000002 a");
            pr.println("two 10000000000003 a");
            pr.println("two 0.000000000000003 a");
        }
    }
}
