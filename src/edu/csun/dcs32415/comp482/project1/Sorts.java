/**
 * @author Daniel Schetritt
 * @due Thursday, September 26, 2017 12:00 PM
 */

package edu.csun.dcs32415.comp482.project1;

import java.util.Arrays;

public class Sorts {
    /**
     * Returns true iff the array is sorted.
     * @param data
     * @return
     */
    public static boolean isSorted(int[] data) {
        if (data.length < 2) {
            return true;
        }
        for (int i = 1; i < data.length; i++) {
            if (data[i-1] > data[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Implementation of Merge sort that preserves the original array, just like the book.
     * @param data The array to sort
     * @return A sorted copy of the array
     */
    public static int[] mergeSort(int[] data) {
        if (data.length < 2)
        {
            // Already sorted.
            return data;
        }
        // Cut the data in two halves.
        int middle = data.length/2;
        int[] left = Arrays.copyOfRange(data, 0, middle);
        int[] right = Arrays.copyOfRange(data, middle, data.length);

        //Sort each half separately.
        left = mergeSort(left);
        right = mergeSort(right);

        // Merge back into 1 array.
        return merge(left,right);
    }

    /**
     * Merges two sorted arrays such that they stay sorted, without modifying the original arrays; just like the book.
     * @param left
     * @param right
     * @return the merged array.
     */
    private static int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int nextLeftIndex = 0;
        int nextRightIndex = 0;
        int i = 0;

        //Start merging.

        while (nextLeftIndex < left.length && nextRightIndex < right.length) {
            if (left[nextLeftIndex] < right[nextRightIndex]) {
                merged[i] = left[nextLeftIndex++];
            }
            else {
                merged[i] = right[nextRightIndex++];
            }
            i++;
        }

        // Copy remaining elements from the larger array.

        while (nextLeftIndex < left.length) {
            merged[i++] = left[nextLeftIndex++];
        }
        while (nextRightIndex < right.length) {
            merged[i++] = right[nextRightIndex++];
        }

        return merged;
    }


    /**
     * Sorts the data array using quicksort. This modifies the original array.
     * @param data The array to sort.
     * @return For convenience, it also returns the array.
     */
    public static int[] quickSort(int[] data) {

        return quickSort(data,0,data.length - 1);
    }

    /**
     * Modifies and sorts a subsequence of the data array using quicksort.
     * This implementation uses O(log n) extra space for its recursive calls.
     * @param data The array to sort.
     * @param start The smaller index where the subsequence starts, inclusive.
     * @param end The larger index where the subsequence ends, inclusive.
     * @return For conveniene, it also returns the array.
     */
    public static int[] quickSort (int[] data, int start, int end) {
        // This loop is for tail recursion of the larger partition.
        while (start < end) {
            int pivot = partition(data,start,end);
            int leftSize = pivot - start;
            int rightSize = end - pivot;

            // Perform a recursive call on the smaller partition, and then prep the larger for the next loop iteration.
            if (leftSize < rightSize) {
                data = quickSort(data, start, pivot-1);
                start = pivot + 1;
            }
            else {
                data = quickSort(data, pivot+1, end);
                end = pivot - 1;
            }
        }
        return data;
    }

    /**
     * Modifies a subsequence of the data array such that every element in the subsequence that is less than
     * a random pivot is to the left of it, and every element in the subsequence that is larger than the pivot
     * is to the right of it.
     * @param data The data array
     * @param start the smaller index where the subsequence starts, inclusive
     * @param end the larger index where the subsequence ends, inclusive
     * @return the new position of the pivot.
     */
    public static int partition (int[] data, int start, int end)  {
        // Pick a random pivot.
        int r = Helpers.rand(start,end);
        int pivot = data[r];

        // Put the pivot at the end for safe keeping.
        swap(data, r, end);

        // This will point to the largest value to the left of the pivot.
        int leftBoundary = start;
        // This will point to the smallest value to the right of the pivot.
        int rightBoundary = end - 1;

        // Do the partitioning until the boundaries cross.
        while (leftBoundary <= rightBoundary) {
            // Seek rightwards to the next out-of-place element smaller than the pivot, but stay in bounds.
            while (data[leftBoundary] <= pivot && leftBoundary <= rightBoundary) {
                leftBoundary++;
            }
            // Seek leftwards to the next out-of-place element larger than the pivot, but stay in bounds.
            while (data[leftBoundary] >= pivot && leftBoundary <= rightBoundary) {
                leftBoundary++;
            }
            // If we are still in bounds,
            if (leftBoundary < rightBoundary) {
                // swap the two out-of-place elements.
                swap(data,leftBoundary,rightBoundary);
            }
        }
        // restore the pivot from the end to its final place.
        swap(data,leftBoundary,end);

        //Return a pointer to the pivot.
        return leftBoundary;
    }


    /**
     * Swaps two elements in an array at the given indices, without a temporary variable.
     * @param a The array
     * @param i The index of the first element to swap.
     * @param j The index of the second element to swap.
     */
    private static void swap(int[] a, int i, int j) {
        if (i != j) {
            a[i] ^= a[j];
            a[j] ^= a[i];
            a[i] ^= a[j];
        }
    }
}
