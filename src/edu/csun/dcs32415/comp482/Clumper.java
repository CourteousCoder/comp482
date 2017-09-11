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
        print(data, average(data,defaultMaxClumps));
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
        builder = builder.append(']');
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
        final int clumpWeight = data.length/nClumps;
        return (int[] d) -> {
            int[] clumpedData = new int[nClumps];

            // Calculate the average of each clump except for the last one.

            //This points to the start of the next clump in d.
            int nextClump = 0;
            for (int i = 0; i < clumpedData.length - 1; i++) {
                int sum = 0;
                for(int j = nextClump; j < nextClump + clumpWeight; j++) {
                    sum += d[j];
                }
                clumpedData[i] = sum / clumpWeight;
                nextClump += clumpWeight;
            }

            //Clump all remaining elements (this could  be different than clumpWeight).
            int sum = 0;
            int count = 0;
            while(nextClump + count < d.length) {
                sum += d[nextClump + count];
                count++;
            }
            clumpedData[clumpedData.length-1] = sum / count;

            return clumpedData;
        };
    }
}
