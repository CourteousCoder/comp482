package edu.csun.dcs32415.comp482;

/**
 * A class containing static functions for clumping (aggregating) the elements of large arrays for ease of analysis and printing.
 */
public class Clumper {
    /**
     * This describes the default max size of a clumped array.
     */
    public static int defaultMaxClumps = 10;

    /**
     * An interface for a function that describes how to clump an array.
     */
    private interface Aggregator {
        /**
         * Aggregates a large array of integers into a smaller array, somehow.
         * @param data  The array to aggregate.
         * @return The clumped array.
         */
        int[] clump(int[] data);
    }

    /**
     * Clumps an array using the default aggregator and prints it in this format: [1,2,3,4,5,6]
     * @param data The array to print.
     */
    public static void print(int[] data) {
        print(data, clump(data));
    }


    /**
     * Clumps an array and prints in this format: [1,2,3,4,5,6,8,9,0]
     * @param data The array to print.
     * @param aggregator A lambda describing the way you want to clump the data.
     */
    public static void print (int[] data, Aggregator aggregator) {
        data = aggregator.clump(data);
        // Initialize a string builder at the smallest size required to display 1-digit integers, commas, and brackets.
        StringBuilder builder = new StringBuilder(2*(data.length + 1));
        builder = builder.append('[');
        for (int element : data) {
            builder = builder.append(element).append(',');
        }
        builder = builder.deleteCharAt(builder.lastIndexOf(",")).append(']');
        System.out.println(builder.toString());
    }

    /**
     * Clumps the data using a default technique.
     * @param data
     * @return
     */
    public static int[] clump (int[] data) {
        return clump(data,average(data, defaultMaxClumps));
    }


    /**
     * Clumps the given array using the given clumping-technique.
     * @param data The array to aggregate.
     * @param aggregator The lambda expression describing the technique with which to aggregate.
     * @return The same data, aggregated into a smaller array.
     */
    public static int[] clump (int[] data, Aggregator aggregator) {
        return aggregator.clump(data);
    }

    public static Aggregator average(int[] data, int nClumps) {
        //  For small arrays, it is already clumped.
        if (data.length <= nClumps) {
            // So do nothing to it.
            return d -> d;
        }

        // The number of elements from data that go into a single clump.
        final int clumpWeight;

        // The weight of the left-most clump in the new array.
        final int firstClumpWeight;

        // Ensure that the number of clumps is exactly nClumps.
        if (data.length % nClumps == 0) {
            clumpWeight = data.length / nClumps;
            firstClumpWeight = clumpWeight;
        } else {
            clumpWeight = 1 + data.length / nClumps;
            firstClumpWeight = data.length % nClumps;
        }

        return d -> {
            int[] clumpedData = new int[nClumps];

            //First we calculate the average of the first clump because it might be smaller.
            int average = 0;
            for (int j = 0; j < firstClumpWeight; j++) {
                average += d[j];
            }
            clumpedData[0] = average / firstClumpWeight;

            //Then we calculate the average of the remaining clumps.

            for (int i = 1; i < clumpedData.length; i++) {
                average = 0;
                for (int j = 0; j < clumpWeight; j++) {
                    average += d[ j + i*clumpWeight ];
                }
                clumpedData[i] = average / clumpWeight;
            }
            return clumpedData;
        };
    }
}
