package edu.csun.dcs32415.comp482.project1;

import java.io.Serializable;

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
        this.count = 1;
        //TODO: run trial.
    }

    /**
     * Runs both experience multiple times.
     * @param n  The size of the array on which to run the trial.
     * @param count The number of times to run the experiment.
     */
    public void run(int n, int count) {
        Trial currentTrial = new Trial();
        for (int i = 0; i < count; i++) {
            currentTrial.run(n);
            add(currentTrial);
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

    private void add(Trial t) {
        this.n += t.n;
        this.count += t.count;
        this.mergeSortWins += t.mergeSortWins;
        this.quickSortWins += t.quickSortWins;
        this.mergeSortRuntime += t.mergeSortRuntime;
        this.quickSortRuntime += t.quickSortRuntime;
    }


    /**
     * Creates an array of random values between min and max, inclusive
     * @param size The size of the array.
     * @param min The smallest possible value for the data.
     * @param max The largest possible value fo rthe data.
     * @return The new array.
     */
    private static int[] createRandomData(int size, int min, int max) {
        int[] data = new int[size];
        for (int i = 0; i < data.length; i++) {
            data[i] = Helpers.rand(min,max);
        }
        return data;
    }

}
