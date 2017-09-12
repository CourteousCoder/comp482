package edu.csun.dcs32415.comp482.project1;

public class Helpers {
    /**
     * Returns a random integer in the given inclusive range.
     * @return
     */
    public static int rand(int min, int max) {
        return min + (int)(Math.random() * (max-min));
    }

    /**
     * Computes n * log(n) with logarithmic base = 2.
     * @param n Any positive integer.
     * @return n * log (n)
     */
    public static double nLogN(int n) {
        return n * Math.log(n) / Math.log(2);
    }
}
