package edu.csun.dcs32415.comp482.project2;

import java.util.*;

public class Knapsack {
    private int capacity;
    private int size;
    private Item[] items;

    public Knapsack() {
        size = 0;
        capacity = 0;
        items = new Item[1];
        items[0] = new Item(0,-1,-1);
    }

    /**
     * Converts k to a bit array of size n.
     * @param k
     * @param n
     * @return
     */
    public static int[] generateSubset(int k, int n) {
        int[] result = new int[n];
        String s  = Integer.toBinaryString(k);

        // Add padding bits
        int paddingAmount = n - s.length();
        for (int i = 0; i < paddingAmount; i++) {
            result[i] = 0;
        }

        // Copy the bits from the binary string.
        for (int i = paddingAmount; i < n; i++) {
            result[i] = s.charAt(i - paddingAmount) - '0';
        }

        return result;
    }

    public void GreedyApproximateSolution(int size, int[] weights, int[] benefits, int capacity) {
        setItems(size, weights, benefits, capacity);
        int i = 0;
        int sum = 0;
        boolean isFull = false;
        while (i < items.length && !isFull){
            if(sum + items[i].weight <= capacity) {
                items[i].use();
                sum += items[i].weight;
            } else {
                isFull = true;
            }
            i++;
        }
        System.out.println(this);
        printSet();
    }

    private int[] getWeights() {
        int[] weights = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            weights[i] = items[i].weight;
        }
        return weights;
    }

    private int[] getBenefits() {
        int[] benefits = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            benefits[i] = items[i].benefit;
        }
        return benefits;
    }

    /**
     * Gets the contents of the knapsack in name order; i.e., the items used.
     * @return
     */
    private int[] getContents() {
        int[] contents;
        //Figure otu which items should go into the list.
        List<Integer> usedItemNames = new LinkedList<Integer>();
        for (Item i : items) {
            if (i.isUsed()) {
                usedItemNames.add(Integer.valueOf(i.name));
            }
        }
        //Sort the names.
        usedItemNames.sort(Comparator.comparing(integer -> integer.intValue()));

        // Copy them into an array.
        contents = new int[usedItemNames.size()];
        int i = 0;
        for (Integer name : usedItemNames) {
            contents[i] = name.intValue();
            i++;
        }
        return contents;
    }

    private int weightSum() {
        int sum = 0;
        for (Item i : items) {
            if (i.isUsed()) {
                sum+=i.weight;
            }
        }
        return sum;
    }

    private int benefitSum() {
        int sum = 0;
        for (Item i : items) {
            if (i.isUsed()) {
                sum+=i.benefit;
            }
        }
        return sum;
    }

    private void setItems(int size, int[] weights, int[] benefits, int capacity) {
        this.capacity = capacity;
        this.size = size;
        this.items = new Item[size + 1];
        this.items[0] = new Item(0,-1,-1);
        for (int i = 0; i < this.size; i++) {
            this.items[i+1] = new Item(i,weights[i], benefits[i]);
        }
    }

    /**
     * Makes a string from an array of integers like this: "[1,4,2,4,6,8]".
     * @param data
     * @return A pretty string representation of data.
     */
    private static String beautifyArray(int[] data) {
        // Initialize a string builder at the smallest size required to display 1-digit integers, commas, and brackets.
        StringBuilder builder = new StringBuilder(2 * data.length + 1);
        builder = builder.append('[');
        for (int element : data) {
            builder = builder.append(element).append(',');
        }

        // Remove trailing comma.
        int lastCharIndex = builder.length() - 1;
        if (builder.charAt(lastCharIndex) == ',') {
            builder = builder.deleteCharAt(lastCharIndex);
        }

        builder = builder.append(']');
        return builder.toString();
    }

    /**
     * Prints the set of items currently used in the knapsack, the sum of the weights and the sum of the benefits.
     */
    private void printSet() {
        StringBuilder builder = new StringBuilder(beautifyArray(getContents()));
        //Change brackets to curly braces
        builder = builder.replace(0,1,"{ ").replace(builder.length()-1,builder.length()," }");
        System.out.printf("Optimal set= %s weight sum = %d benefit sum = %d", builder.toString(), weightSum(), benefitSum());
    }

    @Override
    public String toString() {
        int[] weights = getWeights();
        int[] benefits = getBenefits();
        StringBuilder builder = new StringBuilder("Knapsack Problem Instance");
        builder = builder.append("\nNumber of items = ").append(items.length);
        builder = builder.append("  Knapsack Capacity = ").append(capacity);
        builder = builder.append("\nInput weights:  ").append(beautifyArray(weights));
        builder = builder.append("\nInput benefits: ").append(beautifyArray(benefits));
        return builder.toString();
    }

    private void restoreOriginalOrder() {
        Arrays.sort(items,Comparator.comparing( i -> i.name));
    }

    private void sortByRatio() {

        Arrays.sort(items, Comparator.comparing( i -> i.ratio));

        // Descending order.
        Collections.reverse(Arrays.asList(items));
    }

    private void reset() {
        for (Item i : items) {
            i.clearUsed();
        }
        restoreOriginalOrder();
    }

    private class Item {
        public final int name;
        public final int weight;
        public final int benefit;
        public double ratio;
        private boolean isUsed;

        public Item(int name, int weight, int benefit) {
            this.name = name;
            this.weight = weight;
            this.benefit = benefit;
            this.ratio = name == 0 ? 0 : benefit / (double) weight;
            this.isUsed = false;
        }

        public boolean isUsed() {
            return isUsed;
        }

        public void use() {
            if (name != 0) {
                isUsed = true;
            }
        }

        public void clearUsed() {
            isUsed = false;
        }
    }
}
