package edu.csun.dcs32415.comp482.project2;

public class KnapsackDriver {


    public static void main(String[] args) {
        int[] weights = { 60, 50, 60, 50, 70, 70, 45};
        int[] benefits = {180, 95, 40, 95, 40, 40, 105};
        Knapsack knap = new Knapsack();
        knap.GreedyApproximateSolution(weights.length, weights,benefits,20);
    }
}
