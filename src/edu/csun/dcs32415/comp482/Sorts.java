package edu.csun.dcs32415.comp482;

import java.util.Arrays;

public class Sorts {
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
}
