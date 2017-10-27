package edu.csun.dcs32415.comp482.project2;

public class KnapsackDriver {


    public static void main(String[] args) {
        int[] weights = { -1, 60, 50, 60, 50, 70, 70, 45};
        int[] benefits = {-1, 180, 95, 40, 95, 40, 40, 105};
        Knapsack knap = new Knapsack();
        knap.GreedyApproximateSolution(weights.length -1, weights,benefits,100);
    }
}
