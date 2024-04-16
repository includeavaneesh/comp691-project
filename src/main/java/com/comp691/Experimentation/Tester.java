package com.comp691.Experimentation;

import java.io.IOException;

import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class Tester {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        AlgorithmAnalysis objectAnalysis = new AlgorithmAnalysis();
        objectAnalysis.k_DependencyTest();
    }
}
