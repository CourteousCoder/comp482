package edu.csun.dcs32415.comp482.project1;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a row in either experiment's table.
 */
public class Trial {
    private int n;
    private int count;
    private int mergeSortWins;
    private int quickSortWins;
    private long mergeSortRuntime;
    private long quickSortRuntime;

    public Trial() {
        this.n = 0;
        this.count = 0;
        this.mergeSortWins = 0;
        this.quickSortWins = 0;
        this.mergeSortRuntime = 0;
        this.quickSortRuntime = 0;
    }

    public Trial(int n, int count) {
        this();
        run(n,count);
    }

    /**
     * Runs both experiments once for a random array of size n.
     * Note: This array is identical for both quickSort and mergeSort.
     * @param n The size of the array on which to run the trial.
     */
    public void run(int n) {
        this.n = n;
        this.count++;
        String message;

        // Create the arrays to sort.
        int[] mergeSortMe = Helpers.createRandomData(n,1,1000000);
        int[] quickSortMe = Arrays.copyOf(mergeSortMe, mergeSortMe.length);
        System.out.println(String.format("Input (n=%d):",n));
        Clumper.print(quickSortMe);

        //Set up timers.
        long start, finish, ms, qs;

        // Time merge sort.
        start = System.nanoTime();
        mergeSortMe = Sorts.mergeSort(mergeSortMe);
        finish = System.nanoTime();
        ms = finish - start;

        //Verify it is sorted.
        if(Sorts.isSorted(mergeSortMe)) {
            message = "PASS";
        }
        else {
            message = "FAIL";
        }
        System.out.println(String.format("Merge Sort (%s):",message));
        Clumper.print(mergeSortMe);

        // Time quick sort.
        start = System.nanoTime();
        quickSortMe = Sorts.quickSort(quickSortMe);
        finish = System.nanoTime();
        qs = finish - start;

        //Verify it is sorted.
        if(Sorts.isSorted(quickSortMe)) {
            message = "PASS";
        }
        else {
            message = "FAIL";
        }

        //Update our class variables.
        this.mergeSortRuntime += ms;
        this.quickSortRuntime += qs;

        //Which algorithm won? A tie means they both win.
        if (ms <= qs) {
            this.mergeSortWins++;
        }
        if (qs <= ms) {
            this.quickSortWins++;
        }

        System.out.println(String.format("Quick Sort (%s):",message));
        Clumper.print(quickSortMe);

    }

    /**
     * Runs both experience multiple times.
     * @param n  The size of the array on which to run the trial.
     * @param count The number of times to run the experiment.
     */
    public void run(int n, int count) {
        for (int i = 0; i < count; i++) {
            run(n);
        }
    }

    public String getExperiment1() {
        return String.format("%d,%d,%d,%d\n",n,count,mergeSortWins,quickSortWins);
    }

    public String getExperiment2() {
        double meanMergeSortRuntime = this.mergeSortRuntime / (1.0d * this.count);
        double meanQuickSortRuntime = this.quickSortRuntime / (1.0d * this.count);
        double mergeSortRatio = mergeSortRuntime / Helpers.nLogN(this.n);
        double quickSortRatio = quickSortRuntime / Helpers.nLogN(this.n);
        return String.format("%d,%f,%f,%f,%f\n",
                n,meanMergeSortRuntime,mergeSortRatio,meanQuickSortRuntime,quickSortRatio);

    }

}
