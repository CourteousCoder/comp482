package edu.csun.dcs32415.comp482.project2;

import java.io.*;

public class Knapsack {

    private int maxWeight;
    private int[] weights;
    private int[] benefits;

    public Knapsack(int maxWeight, int[] weights, int[] benefits) {
        this.maxWeight = maxWeight;
        this.weights = weights;
        this.benefits = benefits;
    }

    /**
     * Converts k to a bit array of size n.
     * @param k
     * @param n
     * @return
     */
    public static int[] generateSubset(int k, int n) {
        int[] result = new int[n];
        String s  = Integer.toBinaryString(k);
        int paddingAmount = n - s.length();
        for (int i = 0; i < paddingAmount; i++) {
            result[i] = 0;
        }
        for (int i = paddingAmount; i < n; i++) {
            result[i] = (int) (s.charAt(i - paddingAmount) - '0');
        }
        for ( int bit : result) {
            System.out.print(bit);
        }
        return result;
    }

}
