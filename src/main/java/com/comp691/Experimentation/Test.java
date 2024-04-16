package com.comp691.Experimentation;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.comp691.BlindOracle;
import com.comp691.LeastRecentlyUsed;
import com.comp691.SequenceGenerator;
import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class Test {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        // int k = 5;
        // int N = 15;
        // int n = 10000;

        // // Extra parameters for locality and noise calculations
        // double e = 0.001;
        // double tau = 0.99;
        // int w = 10;

        // List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
        // List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
        // List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

        // BlindOracle BO = new BlindOracle();
        // int boPageFaults = BO.blindOracle(k, pageSequence, predictedhSeq);
        // LeastRecentlyUsed LRU = new LeastRecentlyUsed();
        // int lruPageFaults = LRU.leastRecentlyUsed(k, pageSequence);
        // BlindOracle OPT = new BlindOracle();
        // int optPageFaults = OPT.blindOracle(k, pageSequence, hSeq);

        // System.out.println("Test: # of page faults incurred by OPT: " + optPageFaults);
        // System.out.println("Test: # of page faults incurred by LRU: " + lruPageFaults);
        // System.out.println("Test: # of page faults incurred by Blind Oracle: " + boPageFaults);

        // System.out.println("Test: executed successfully!");
        List<Double> x = NumpyUtils.linspace(-3, 3, 100);
        List<Double> y = x.stream().map(xi -> Math.sin(xi) + Math.random()).collect(Collectors.toList());

        Plot plt = Plot.create();
        plt.plot().add(x, y, "o").label("sin");
        plt.legend().loc("upper right");
        plt.title("scatter");
        plt.show();
    }
}
