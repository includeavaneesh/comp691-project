package com.comp691.Experimentation;

import java.io.IOException;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class ExperimentationGraph {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        AlgorithmAnalysis evaluator = new AlgorithmAnalysis();

        // K-Dependency evaluation regime 1
        evaluator.k_DependencyTest(0.01, 0.99, 10, 0.4, "Dependence on k (Regime 1)");

        // K-Dependency evaluation regime 2
        evaluator.k_DependencyTest(0.99, 0.99, 10, 0.4, "Dependence on k (Regime 2)");

        evaluator.w_DependencyTest(0.01, 0.99, 5, 0.01, "Dependence on w (Regime 1)");
        evaluator.w_DependencyTest(0.99, 0.99, 5, 0.01, "Dependence on w (Regime 2)");

        evaluator.e_DependencyTest(25, 0.01, 5, 0.3, "Dependence on e (Low tau)");
        evaluator.e_DependencyTest(25, 0.99, 5, 0.3, "Dependence on e (High tau)");

        evaluator.tau_DependencyTest(0.99, 5, 25, 0.1, "Dependence on tau (High e)");
        evaluator.tau_DependencyTest(0.01, 5, 25, 0.1, "Dependence on tau (Low e)");

    }
}
