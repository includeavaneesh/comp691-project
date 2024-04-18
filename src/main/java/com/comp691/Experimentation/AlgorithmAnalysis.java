package com.comp691.Experimentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.comp691.BlindOracle;
import com.comp691.CombinedAlgorithm;
import com.comp691.LeastRecentlyUsed;
import com.comp691.SequenceGenerator;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

/**
 * Regime 1: where OPT is significantly better than BlindOracle with hˆ values
 * which is significantly better than LRU
 * e = 0.01 (closer to 1 --> BO >> LRU, closer to 0 --> LRU >> BO)
 * tau = 0.99 (closer to 1 --> BO >> OPT, closer to 0 --> BO = OPT)
 * OPT performs 30% better than BO, and BO performs 12% better than LRU
 * 
 * Regime 2 where OP T is significantly better than LRU which is significantly
 * better than BlindOracle with hˆ values
 * e = 0.99
 * tau = 0.99
 * OPT performs 50% better than LRU, and LRU performs 76% better than Blind
 * Oracle
 */
public class AlgorithmAnalysis {

    /**
     * 
     * @param k
     * @param N
     * @param n
     * @param e
     * @param tau
     * @param w
     * @param thr
     */
    private int[] trial(int k, int N, int n, double e, double tau, int w, double thr) {
        List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
        List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
        List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

        // OPT Page faults
        BlindOracle OPT = new BlindOracle();
        int optPageFaults = OPT.blindOracle(k, pageSequence, hSeq);

        // Blind Oracle Page faults
        BlindOracle BO = new BlindOracle();
        int boPageFaults = BO.blindOracle(k, pageSequence, predictedhSeq);

        // LRU Page faults
        LeastRecentlyUsed LRU = new LeastRecentlyUsed();
        int lruPageFaults = LRU.leastRecentlyUsed(k, pageSequence);

        // Combined Algorithm Page faults
        CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
        int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

        return new int[] { optPageFaults, boPageFaults, lruPageFaults, caPageFaults };
    }

    private double[] batchTest(int k, int N, double e, double tau, int w, double thr) {
        int n = 10_000;

        double avg_optPageFaults = 0;
        double avg_boPageFaults = 0;
        double avg_lruPageFaults = 0;
        double avg_caPageFaults = 0;

        for (int iteration = 1; iteration <= 100; iteration++) {
            int[] result = new int[4];
            result = trial(k, N, n, e, tau, w, thr);

            avg_optPageFaults += result[0];
            avg_boPageFaults += result[1];
            avg_lruPageFaults += result[2];
            avg_caPageFaults += result[3];
        }

        avg_optPageFaults = avg_optPageFaults / 100;
        avg_boPageFaults = avg_boPageFaults / 100;
        avg_lruPageFaults = avg_lruPageFaults / 100;
        avg_caPageFaults = avg_caPageFaults / 100;

        return new double[] { avg_optPageFaults, avg_boPageFaults, avg_lruPageFaults, avg_caPageFaults };

    }

    public void k_DependencyTest(double e, double tau, int w, double thr, String figureName)
            throws IOException, PythonExecutionException {

        List<Double> OPT = new ArrayList<Double>();
        List<Double> BlindOracle = new ArrayList<Double>();
        List<Double> LeastRecentlyUsed = new ArrayList<Double>();
        List<Double> CombinedAlgorithm = new ArrayList<Double>();
        List<Integer> kList = new ArrayList<Integer>();
        for (int k = 1; k <= 25; k++) {
            int N = 5 * k;
            double[] results = batchTest(k, N, e, tau, w, thr);
            OPT.add(results[0]);
            BlindOracle.add(results[1]);
            LeastRecentlyUsed.add(results[2]);
            CombinedAlgorithm.add(results[3]);
            kList.add(k);

        }

        Plot plt = Plot.create();
        plt.title(figureName);
        plt.ylabel("Page Faults");
        plt.xlabel("Cache Size");

        plt.plot().add(kList, OPT).label("OPT");
        plt.plot().add(kList, BlindOracle).label("Blind Oracle");
        plt.plot().add(kList, LeastRecentlyUsed).label("Least Recently Used");
        plt.plot().add(kList, CombinedAlgorithm).label("Combined Algorithm");
        plt.legend().loc("upper right");
        plt.savefig("./src/main/resources/results/" + figureName + ".png").dpi(1000);
        plt.executeSilently();

    }

    public void w_DependencyTest(double e, double tau, int k, double thr, String figureName)
            throws IOException, PythonExecutionException {
        List<Double> OPT = new ArrayList<Double>();
        List<Double> BlindOracle = new ArrayList<Double>();
        List<Double> LeastRecentlyUsed = new ArrayList<Double>();
        List<Double> CombinedAlgorithm = new ArrayList<Double>();
        List<Integer> wList = new ArrayList<Integer>();

        int N = 200;
        for (int w = 5; w <= 100; w = w + 5) {

            double[] results = batchTest(k, N, e, tau, w, thr);
            OPT.add(results[0]);
            BlindOracle.add(results[1]);
            LeastRecentlyUsed.add(results[2]);
            CombinedAlgorithm.add(results[3]);
            wList.add(w);

        }

        Plot plt = Plot.create();
        plt.title(figureName);
        plt.ylabel("Page Faults");
        plt.xlabel("W");

        plt.plot().add(wList, OPT).label("OPT");
        plt.plot().add(wList, BlindOracle).label("Blind Oracle");
        plt.plot().add(wList, LeastRecentlyUsed).label("Least Recently Used");
        plt.plot().add(wList, CombinedAlgorithm).label("Combined Algorithm");
        plt.legend().loc("upper right");
        plt.savefig("./src/main/resources/results/" + figureName + ".png").dpi(1000);
        plt.executeSilently();
    }

    public void e_DependencyTest(int w, double tau, int k, double thr, String figureName)
            throws IOException, PythonExecutionException {
        List<Double> OPT = new ArrayList<Double>();
        List<Double> BlindOracle = new ArrayList<Double>();
        List<Double> LeastRecentlyUsed = new ArrayList<Double>();
        List<Double> CombinedAlgorithm = new ArrayList<Double>();
        List<Double> eList = new ArrayList<Double>();

        int N = 50;
        for (double e = 0.1; e < 1; e = e + 0.1) {

            double[] results = batchTest(k, N, e, tau, w, thr);
            OPT.add(results[0]);
            BlindOracle.add(results[1]);
            LeastRecentlyUsed.add(results[2]);
            CombinedAlgorithm.add(results[3]);
            eList.add(e);

        }

        Plot plt = Plot.create();
        plt.title(figureName);
        plt.ylabel("Page Faults");
        plt.xlabel("e");

        plt.plot().add(eList, OPT).label("OPT");
        plt.plot().add(eList, BlindOracle).label("Blind Oracle");
        plt.plot().add(eList, LeastRecentlyUsed).label("Least Recently Used");
        plt.plot().add(eList, CombinedAlgorithm).label("Combined Algorithm");
        plt.legend().loc("upper right");
        plt.savefig("./src/main/resources/results/" + figureName + ".png").dpi(1000);
        plt.executeSilently();
    }

    public void tau_DependencyTest(double e, int k, int w, double thr, String figureName)
            throws IOException, PythonExecutionException {

        List<Double> OPT = new ArrayList<Double>();
        List<Double> BlindOracle = new ArrayList<Double>();
        List<Double> LeastRecentlyUsed = new ArrayList<Double>();
        List<Double> CombinedAlgorithm = new ArrayList<Double>();
        int N = 50;
        List<Double> tauList = new ArrayList<Double>();
        for (double tau = 0.1; tau < 1; tau += 0.1) {

            double[] results = batchTest(k, N, e, tau, w, thr);
            OPT.add(results[0]);
            BlindOracle.add(results[1]);
            LeastRecentlyUsed.add(results[2]);
            CombinedAlgorithm.add(results[3]);
            tauList.add(tau);

        }

        Plot plt = Plot.create();
        plt.title(figureName);
        plt.ylabel("Page Faults");
        plt.xlabel("Tau");

        plt.plot().add(tauList, OPT).label("OPT");
        plt.plot().add(tauList, BlindOracle).label("Blind Oracle");
        plt.plot().add(tauList, LeastRecentlyUsed).label("Least Recently Used");
        plt.plot().add(tauList, CombinedAlgorithm).label("Combined Algorithm");
        plt.legend().loc("upper right");
        plt.savefig("./src/main/resources/results/" + figureName + ".png").dpi(1000);
        plt.executeSilently();

    }

}
