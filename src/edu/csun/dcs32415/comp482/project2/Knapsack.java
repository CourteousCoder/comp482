package edu.csun.dcs32415.comp482.project2;


import java.util.*;

public class Knapsack {
    private int capacity;
    private int n;
    private SortedSet<Item> items;
    // A collection of items are being put into the knapsack
    private SortedSet<Item> contents;
    // A collection of items that are NOT being put into the knapsack
    private SortedSet<Item> trash;

    // Describes an ordering of items by their name.
    private Comparator<Item> byName;
    // Describes an ordering of items by ratio of benefit/weight
    private Comparator<Item> byRatio;


    public Knapsack(int weightCapacity, int[] weights, int[] benefits) {
        n = weights.length-1;
        capacity = weightCapacity;

        // Define an ordering of items by name.
        byName = Comparator.comparing(item -> item.name);
        // Define an ordering  of items, first by ratio, then by name (if ratios are the same).
        byRatio = (Item a, Item b) -> {
            Double ratioA = a.ratio;
            Double ratioB = b.ratio;
            int ratioComparison = ratioA.compareTo(ratioB);
            if (ratioComparison != 0) {
                return ratioComparison;
            } else {
                Integer nameA = a.name;
                Integer nameB = b.name;
                return nameA.compareTo(nameB);
            }
        };

        items = new TreeSet<>(byName);
        contents = null;
        trash = null;
        // Fill the set of available items using the given weights and benefits. Ignore the -1 element at index 0.
        for (int i = 1; i < weights.length; i++) {
            items.add(new Item(i, weights[i], benefits[i]));
        }

        print();
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

    public void BruteForceSolution() {
        SolutionOutputter solutionOutputter;
        // Using a set of sets avoids duplicates such as { {1,4}, {4,1} }
        Set<SortedSet<Item>> uniqueSolutions = new HashSet<>();
        int largestBenefit = 0;
        long pow2n = (int)Math.floor(Math.pow(2,n));
        for (int k = 0; k < pow2n; k++) {
            itemizeSubset(generateSubset(k,n));
            solutionOutputter = new SolutionOutputter(contents);

            // If this solution is feasible.
            int currentWeight = solutionOutputter.sum(solutionOutputter.getWeights());
            if(currentWeight <= capacity) {
                // If this solution is more optimal than the others checked so far.
                int currentBenefit = solutionOutputter.sum(solutionOutputter.getBenefits());
                if (currentBenefit > largestBenefit) {
                    // Empty our knapsack because the solutions so far are not optimal.
                    uniqueSolutions.clear();
                }
                // If this solution is at least as good as the ones checked so far.
                if (currentBenefit >= largestBenefit) {
                    // Then so far, we can say it's optimal.
                    SortedSet<Item> solution = new TreeSet<>(byName);
                    solution.addAll(contents);
                    // Add this solution into our set of solutions.
                    uniqueSolutions.add(solution);
                    // Update our definition of optimal.
                    largestBenefit = currentBenefit;
                }
            }
        }

        // Output the results.
        for (SortedSet<Item> solution : uniqueSolutions) {
            solutionOutputter = new SolutionOutputter(solution);
            solutionOutputter.print();
        }

    }

    public void GreedyApproximateSolution() {
        reset();
        contents = new TreeSet<>(byName);

        // Let the trash sort the items by ratio in ascending order.
        trash = new TreeSet<>(byRatio);
        trash.addAll(items);
        items.clear();

        // Take items out of the trash, and put them into the knapsack until it's full or until we take all items.
        int spaceRemaining = capacity;
        while(!trash.isEmpty() && spaceRemaining > 0) {
            // Take the item with the highest ratio.
            Item currentItem = trash.last();
            spaceRemaining -= currentItem.weight;
            // If it fits.
            if(spaceRemaining > 0) {
                // Take the item from the trash.
                trash.remove(currentItem);
                contents.add(currentItem);
            }
        }

        SolutionOutputter solutionOutputter = new SolutionOutputter(contents);
        solutionOutputter.print();
    }

    /**
     * Transforms a bitmap into a two sets of items.
     * Stores the 1's as their corresponding items into this.contents
     * Stores the 0's as their corresponding items into this.trash
     * @param subset A bitmap as an integer array that defines the subset.
     */
    private void itemizeSubset(int[] subset) {
        reset();
        contents = new TreeSet<>(byName);
        trash = new TreeSet<>(byName);

        // This works because the items are in name-order and because items.size() == subset.length
        for (int i=0; !items.isEmpty(); i++) {
            // Get the i'th (the next) item.
            Item currentItem = items.first();
            // If the i'th item is in the subset.
            if (subset[i] == 1) {
                // Add it to the knapsack.
                contents.add(currentItem);
            } else {
                // Throw it in the trash.
                trash.add(currentItem);
            }
            // Remove the current item from the set of items that are to be considered.
            items.remove(currentItem);
        }
    }

    /**
     * Print the knapsack instance.
     */
    public void print() {
        SolutionOutputter solutionOutputter = new SolutionOutputter(this.items);
        List<Integer> weights = solutionOutputter.getWeights();
        List<Integer> benefits = solutionOutputter.getBenefits();

        // Put back the -1 into index 0.
        weights.add(0,new Integer(-1));
        benefits.add(0,new Integer(-1));

        StringBuilder builder = new StringBuilder("Knapsack Problem Instance");
        builder = builder.append("\nNumber of items = ").append(this.n);
        builder = builder.append("  Knapsack Capacity = ").append(this.capacity);
        builder = builder.append("\nInput weights:  ").append(weights);
        builder = builder.append("\nInput benefits: ").append(benefits);

        System.out.println(builder.toString());
    }

    private void reset() {
        if (trash != null) {
            items.addAll(trash);
            trash = null;
        }
        if (contents != null) {
            items.addAll(contents);
            contents = null;
        }
    }

    private class Item {
        public final int name;
        public final int weight;
        public final int benefit;
        public double ratio;
        private int hashCode;

        public Item(int name, int weight, int benefit) {
            this.name = name;
            this.weight = weight;
            this.benefit = benefit;
            this.ratio =  benefit / (double) weight;

            //Cache the hashcode because Items are immutable.
            int primeNumber = 251;
            hashCode = name;
            hashCode += (benefit * primeNumber);
            hashCode += (weight  * primeNumber);
        }

        @Override
        public boolean equals(Object o) {
            Item other = (Item) o;
            return other.name == name && other.weight == weight && other.benefit == benefit;
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }

        @Override
        public String toString() {
            return String.format("(#%d,w:%d,b:%d,r:%2.3f)",name,weight,benefit,ratio);
        }
    }

    /**
     * Helper class for outputting collections and item sets.
     */
    private class SolutionOutputter {
        private List<Integer> names;
        private List<Integer> weights;
        private List<Integer> benefits;

        public SolutionOutputter(Set<Item> itemSet) {
            names = new ArrayList<>();
            weights = new ArrayList<>();
            benefits = new ArrayList<>();
            for (Item item : itemSet) {
                names.add(new Integer(item.name));
                weights.add(new Integer(item.weight));
                benefits.add(new Integer(item.benefit));
            }
        }

        public List<Integer> getNames() {
            return names;
        }

        public List<Integer> getWeights() {
            return weights;
        }

        public List<Integer> getBenefits() {
            return benefits;
        }

        /**
         * Sum a list of integers.
         * @param list
         * @return
         */
        public int sum(List<Integer> list) {
            int result = 0;
            for (Integer integer : list) {
                result += integer.intValue();
            }
            return result;
        }

        public void printMatrix(int[][] matrix, int spacing, boolean withLabels) {
            if (withLabels) {
                System.out.printf(String.format("%%%ds",spacing), "B[k,w]");
                for (int j=1; j <= matrix[0].length; j++) {
                    System.out.printf(String.format("%%%ds",spacing), j);
                }
                System.out.println();
            }
            for(int i=0; i<matrix.length; i++){
                if (withLabels) {
                    System.out.printf(String.format("%%%ds",spacing), i+1);
                }
                for(int j=0; j<matrix[i].length; j++){
                    System.out.printf(String.format("%%%ds",spacing), matrix[i][j]);
                }
                System.out.println();
            }
        }

        /**
         * Prints the given set of items, the sum of the weights and the sum of the benefits.
         */
        public void print() {
            StringBuilder builder = new StringBuilder(names.toString());
            //Change brackets to curly braces
            builder = builder.replace(0,1,"{ ").replace(builder.length()-1,builder.length()," }");
            System.out.printf("Optimal set = %s weight sum = %d benefit sum = %d%n", builder.toString(), sum(weights), sum(benefits));
        }
    }
}
