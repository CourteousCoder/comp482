package edu.csun.dcs32415.comp482.project2;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class KnapsackDriver {


    public static void main(String[] args) {
        runInstructorTestCases();
        System.out.println("=================Experiments===================\nTrying to suppress output...");
        double maximumError = 0;
        double averageError = 0;
        double stdev = 0;
        List<Double> approximationErrors;
        // Suppress output
        PrintStream sysout = System.out;
        try {
            System.setOut(new PrintStream("/dev/null"));
            sysout.println("Success!");
        }
        catch (FileNotFoundException e) {
            sysout.println("Could not suppress output to '/dev/null'. Experimental output will be displayed.");
        }

        sysout.print("Running Experiments");
        approximationErrors = new ArrayList<>();
        for (int n = 0; n < 1000; n++) {
            Double error = runExperiment(n);
            if (n % 10 == 0) {
                sysout.print('.');
            }
            //Sometimes the approximate solution thinks no items will fit. Discard those solutions.
            if (!error.isInfinite()) {
                // Find the average.
                averageError += error.doubleValue();
                // Find the max.
                if (error > maximumError) {
                    maximumError = error;
                }
                approximationErrors.add(error);
            }
        }
        sysout.println("\nDone!");
        System.setOut(sysout);

        averageError /= approximationErrors.size();
        for (Double a : approximationErrors) {
            stdev += Math.pow(a.doubleValue() - averageError, 2);
        }
        stdev = Math.sqrt(stdev/approximationErrors.size());

        System.out.println("\nApproximation Error Percentages (Indexed by the size of each respective input):");
        System.out.println(approximationErrors);
        System.out.printf(
                "Maximum Error = %2.3f%%\tMean = %2.3f%%\tStandard Deviation = %2.3f%%%n",
                maximumError,
                averageError,
                stdev
        );
        if (approximationErrors.size() < 1000) {
            System.out.println("Some of the error percentages were mathematically infinite.");
            System.out.println("This can occur when the approximate solution is 0.");
            System.out.println("These percentages were ignored to give us more meaningful statistics.");
            System.out.printf("A total of %d out of 1000 percentages were infinity.",1000 - approximationErrors.size());
        }

     }

    /**
     * Creates a random Knapsack Instance and tests the greedy approximation
     * for accuracy against the Dynamic Programming solution.
     * @param n The number of items in the instance.
     * @return The approximation error as a percentage.
     */
    private static double runExperiment(int n) {
        Knapsack kp;
        int capacity;
        int[] weights = new int[n+1];
        int[] benefits = new int[n+1];
        weights[0] = benefits[0] = -1;

        capacity = 1 + (int)(25*n * Math.random());
        for (int i = 1; i <= n; i++) {
            weights[i] = 1 + (int)(100 * Math.random());
            benefits[i] = 1 + (int)(100 * Math.random());
        }
        kp = new Knapsack(capacity,weights, benefits);

        // To avoid dividing by zero:
        if (kp.getApproximateBenefit() == kp.getOptimalBenefit()) {
            return 0.0;
        }
        else {
            return 100.0 * kp.getOptimalBenefit() / kp.getApproximateBenefit() - 100.0;
        }
    }

    private static void runInstructorTestCases() {
        Knapsack kp;

        System.out.println("\nTestcase #1");
        int n = 7;
        int[] weights = {-1, 60, 50, 60, 50, 70, 70, 45};
        int W = 100;
        int[] benefits = {-1, 180, 95, 40, 95, 40, 40, 105};
        kp = new Knapsack(W, weights, benefits);
        kp.print();

        System.out.println("\nBrute Force Solution");
        kp.BruteForceSolution();

        System.out.println("\nDynamic Programming Solution");
        kp.DynamicProgrammingSolution(false);


        System.out.println("\nGreedy Approximate Solution");
        kp.GreedyApproximateSolution();

        System.out.println("\nTestcase #2");
        int n2 = 18;
        int[] weights2 = {-1,25,4,2,5,6, 2,7,8,2,1, 1,3,5,8,9,  6,3,2};
        int W2 = 39;
        int[] benefits2 = {-1,75,7,4,3,2,  6,8,7,9,6,  5,4,8,10,8,  1,2,2};
        kp = new Knapsack(W2, weights2, benefits2);
        kp.print();

        System.out.println("\nBrute Force Solution");
        kp.BruteForceSolution();

        System.out.println("\nDynamic Programming Solution");
        kp.DynamicProgrammingSolution(false);


        System.out.println("\nGreedy Approximate Solution");
        kp.GreedyApproximateSolution();



        System.out.println("\nTestcase #3");
        int n3 = 20;
        int[] weights3 = {-1, 10,14,35,12,16, 20,13,7,2,4, 3,10,5,6,17,
                7,9,3,4,3};
        int W3 = 29;
        int[] benefits3 = {-1, 2,13,41,1,12, 5,31,2,41,16,
                2,12,1,13,4, 51,6,12,1,9};
        kp = new Knapsack(W3, weights3, benefits3);
        kp.print();

        System.out.println("\nBrute Force Solution");
        kp.BruteForceSolution();

        System.out.println("\nDynamic Programming Solution");
        kp.DynamicProgrammingSolution(false);


        System.out.println("\nGreedy Approximate Solution");
        kp.GreedyApproximateSolution();

        System.out.println("\nTestcase #4");
        int n4 = 7;
        int[] weights4 = {-1, 2,5,3,2,5,3,7 };
        int W4 = 10;
        int[] benefits4 = {-1, 5,10,5,20,15,5,10};
        kp = new Knapsack(W4, weights4, benefits4);
        kp.print();

        System.out.println("\nBrute Force Solution");
        kp.BruteForceSolution();

        System.out.println("\nDynamic Programming Solution");
        kp.DynamicProgrammingSolution(true);


        System.out.println("\nGreedy Approximate Solution");
        kp.GreedyApproximateSolution();

    }
}
