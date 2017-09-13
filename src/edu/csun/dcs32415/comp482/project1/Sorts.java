/**
 * @author Daniel Schetritt
 * @date Tuesday, September 26, 2017 12:00 PM
 */

package edu.csun.dcs32415.comp482.project1;

public class Sorts {
    /**
     * Returns true iff the array is sorted.
     *
     * @param data
     * @return
     */
    public static boolean isSorted(int[] data) {
        if (data.length < 2) {
            return true;
        }
        for (int i = 1; i < data.length; i++) {
            if (data[i - 1] > data[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sorts the array using  merge sort
     * @param data The array to sort
     */
    public static void mergeSort(int[] data) {
        mergeSort(data,0,data.length-1);
    }


    /**
     * Modifies and sorts a segment of an using merge sort.
     *
     * @param data The array to sort
     * @param start The index of the first element of the segment we want to sort.
     * @param end The index of the last element of the segment we want to sort.
     */
    public static void mergeSort(int[] data, int start, int end) {
        if (start < end) {
            // Cut the data in two halves.
            int middle = (end+start) / 2;

            //Sort each half separately.
            mergeSort(data, start, middle);
            mergeSort(data, middle + 1, end);

            // Merge back into 1 array.
            merge(data, start, middle, end);
        }
    }

    /**
     * Merges two consecutive sorted segments of the array into one sorted segment.
     *
     * @param start The index of the first element of the left-hand-side segment.
     * @param middle The index of the last element of the left-hand-side segment.
     * @param end The index of the last element of the right-hand-side segment.
     */
    private static void merge(int[] data, int start, int middle, int end) {
        // Make a copy of the segment into a buffer.
        int[] buffer = new int[data.length];
        for (int i = start; i <= end; i++) {
            buffer[i] = data[i];
        }

        // Copy the lesser value on each iteration back to the original array.
        int nextDataIndex = start;
        int nextLeftIndex = start;
        int nextRightIndex = middle + 1;

        while (nextLeftIndex <= middle && nextRightIndex <= end) {
            if (buffer[nextLeftIndex] <= buffer[nextRightIndex]) {
                data[nextDataIndex] = buffer[nextLeftIndex++];
            } else {
                data[nextDataIndex] = buffer[nextRightIndex++];
            }
            nextDataIndex++;
        }

        // Copy leftover elements back into the array
        while (nextLeftIndex <= middle) {
            data[nextDataIndex++] = buffer[nextLeftIndex++];
        }
    }



    /**
     * Sorts the data array using quicksort. This modifies the original array.
     * @param data The array to sort.
     */
    public static void quickSort(int[] data) {
        quickSort(data, 0, data.length - 1);
    }

    /**
     * Modifies and sorts a subsequence of the data array using quicksort.
     *
     * @param data  The array to sort.
     * @param start The smaller index where the subsequence starts, inclusive.
     * @param end   The larger index where the subsequence ends, inclusive.
     */
    public static void quickSort(int[] data, int start, int end) {
        if (start < end) {
            int pivot = partition(data, start, end);
            quickSort(data, start, pivot - 1);
            quickSort(data, pivot + 1, end);
        }
    }

    /**
     * Modifies a subsequence of the data array such that every element in the subsequence that is less than
     * a random pivot is to the left of it, and every element in the subsequence that is larger than the pivot
     * is to the right of it.
     *
     * @param data  The data array
     * @param start the smaller index where the subsequence starts, inclusive
     * @param end   the larger index where the subsequence ends, inclusive
     * @return the new position of the pivot.
     */
    public static int partition(int[] data, int start, int end) {
        // Pick a random pivot.
        int r = Helpers.rand(start, end);
        int pivot = data[r];

        // Put the pivot at the end for safe keeping.
        Helpers.swap(data, r, end);

        // This points to the most recently partitioned element such that all elements to the left of 'partition'
        // are less than or equal to the pivot.
        int partition = start;
        // This points to the next element to consider.
        int next = start;

        // Start partitioning elements until we reach the pivot at the end.
        while (next < end) {
            // Check if a small value is on the right side of our partition.
            if (data[next] <= pivot) {
                //Put it in the right place.
                Helpers.swap(data, next, partition);
                // Move the partition.
                partition++;
            }
            next++;
        }

        // The pivot value needs to be at the partition.
        Helpers.swap(data, end, partition);

        return partition;
    }

}

class Helpers {
    /**
     * Returns a random integer in the given inclusive range.
     *
     * @return
     */
    public static int rand(int min, int max) {
        return min + (int) (Math.random() * (max - min));
    }

    /**
     * Computes n * log(n) with logarithmic base = 2.
     *
     * @param n Any positive integer.
     * @return n * log (n)
     */
    public static double nLogN(int n) {
        return n * Math.log(n) / Math.log(2);
    }


    /**
     * Creates an array of random values between min and max, inclusive
     *
     * @param size The size of the array.
     * @param min  The smallest possible value for the data.
     * @param max  The largest possible value fo rthe data.
     * @return The new array.
     */
    public static int[] createRandomData(int size, int min, int max) {
        int[] data = new int[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = Helpers.rand(min, max);
        }
        return data;
    }

    /**
     * Swaps two elements in an array at the given indices, without a temporary variable.
     *
     * @param a The array
     * @param i The index of the first element to swap.
     * @param j The index of the second element to swap.
     */
    public static void swap(int[] a, int i, int j) {
        if (i != j) {
            a[i] ^= a[j];
            a[j] ^= a[i];
            a[i] ^= a[j];
        }
    }
}
