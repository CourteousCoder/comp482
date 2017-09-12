package edu.csun.dcs32415.comp482.project1;

public class SortTester {
    private Trial[] trials;

    public  SortTester() {
        runTests();
    }

    public String getExperiment1() {
        String header = "n,nTrials,# mergeSort Wins,# quicksortWins\n";
        StringBuilder builder = new StringBuilder(header);

        for (Trial trial : trials) {
            builder = builder.append(trial.getExperiment1());
        }

        return builder.toString();
    }

    public String getExperiment2() {
        String header = "n,mergeSort:mean runtime (nanosecs),mergeSort: mean runtime / (n*log2(n)),quickSort: mean runtime (nanosecs),quickSort: mean runtime / (n*log2(n))\n";
        StringBuilder builder = new StringBuilder(header);

        for (Trial trial : trials) {
            builder = builder.append(trial.getExperiment2());
        }

        return builder.toString();
    }

    private final void runTests() {
        trials = new Trial[7];
        for (int i = 0; i < 6; i++) {
            int n = 10 * (i+1);
            trials[i] = new Trial(n,20);
        }
        trials[6] = new Trial(2/*000000*/, 20);
    }
}
