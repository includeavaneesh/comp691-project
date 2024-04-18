package com.comp691.Experimentation;

import java.io.IOException;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class Tester {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        AlgorithmAnalysis objectAnalysis = new AlgorithmAnalysis();

        // // K-Dependency evaluation where OPT << BO << LRU
        // objectAnalysis.k_DependencyTest(0.01, 0.99, 10, 0.4,"Dependence on k (Regime
        // 1)");

        // // K-Dependency evaluation where OPT << LRU << BO
        // objectAnalysis.k_DependencyTest(0.99, 0.99, 10, 0.4,"Dependence on k (Regime
        // 2)");

        objectAnalysis.w_DependencyTest(0.01, 0.99, 5, 0.01, "Dependence on w (Regime 1)");
        objectAnalysis.w_DependencyTest(0.99, 0.99, 5, 0.01, "Dependence on w (Regime 2)");
        // objectAnalysis.w_DependencyTest(0.1, 0.9, 5, 0.3, "Dependence on w");
    }
}
