package edu.csun.dcs32415.comp482.project2;

public class KnapsackDriver {


    public static void main(String[] args) {
        int n = 7;
        int[] weights = {-1, 60, 50, 60, 50, 70, 70, 45};
        int W = 100;
        int[] values = {-1, 180, 95, 40, 95, 40, 40, 105};


        // Print input values as required in Project 2
        (new Knapsack(W,weights, values)).print();

        System.out.println("\nBrute Force Solution");
        Knapsack kp1 = new Knapsack(W, weights, values);
        kp1.BruteForceSolution();


        System.out.println("\nDynamic Programming Solution");
        Knapsack kp3 = new Knapsack(W, weights, values);
       // kp3.DynamicProgrammingSolution(false);


        System.out.println("\nGreedy Approximate Solution");
        Knapsack kp4 = new Knapsack(W, weights, values);
        kp4.GreedyApproximateSolution();
    }
}
