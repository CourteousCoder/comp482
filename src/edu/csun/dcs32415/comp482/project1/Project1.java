package edu.csun.dcs32415.comp482.project1;

import java.io.FileWriter;
import java.io.IOException;

public class Project1 {
    public static void run() {
        SortTester tester = new SortTester();
        System.out.println("=================================");
        String e1 = tester.getExperiment1();
        String e2 = tester.getExperiment2();
        System.out.println(e1);
        System.out.println("---------------------------------");
        System.out.println(e2);

        try{
            FileWriter f = new FileWriter("exp1.csv");
            f.write(e1);
            f = new FileWriter("exp2.csv");
            f.write(e2);
        }
        catch (IOException e) {
            System.out.println("File write error.");
        }
    }
}
