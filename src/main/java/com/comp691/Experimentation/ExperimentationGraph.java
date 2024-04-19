package com.comp691.Experimentation;

/**
 * This code belongs to
 * Student name: Avaneesh Kanshi
 * Student Id: 40273760
 * 
 * Dept. of Computer Science and Software Engineering
 * Concordia University
 * 
 * This is the code for Phase 3 of the project component of COMP 691: Online Algorithms and Competitive Analysis
 * Winter 2024
 */

import java.io.IOException;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;

/**
 * This class implements the graph generation of the experimentation
 */
public class ExperimentationGraph {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        AlgorithmAnalysis evaluator = new AlgorithmAnalysis();

        // K-Dependency evaluation regime 1
        evaluator.k_DependencyTest(0.01, 0.99, 10, 0.4, "Dependence on k (Regime 1)");

        // K-Dependency evaluation regime 2
        evaluator.k_DependencyTest(0.99, 0.99, 10, 0.4, "Dependence on k (Regime 2)");

        // W-Dependency evaluation regime 1
        evaluator.w_DependencyTest(0.01, 0.99, 5, 0.01, "Dependence on w (Regime 1)");
        // W-Dependency evaluation regime 2
        evaluator.w_DependencyTest(0.99, 0.99, 5, 0.01, "Dependence on w (Regime 2)");

        // e-Dependency evaluation low tau
        evaluator.e_DependencyTest(25, 0.01, 5, 0.3, "Dependence on e (Low tau)");
        // e-Dependency evaluation high tau
        evaluator.e_DependencyTest(25, 0.99, 5, 0.3, "Dependence on e (High tau)");

        // tau-Dependency evaluation high e
        evaluator.tau_DependencyTest(0.99, 5, 25, 0.1, "Dependence on tau (High e)");
        // tau-Dependency evaluation low e
        evaluator.tau_DependencyTest(0.01, 5, 25, 0.1, "Dependence on tau (Low e)");

    }
}
