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


    /**
     * Creates an array of random values between min and max, inclusive
     * @param size The size of the array.
     * @param min The smallest possible value for the data.
     * @param max The largest possible value fo rthe data.
     * @return The new array.
     */
    public static int[] createRandomData(int size, int min, int max) {
        int[] data = new int[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = Helpers.rand(min,max);
        }
        return data;
    }
}
