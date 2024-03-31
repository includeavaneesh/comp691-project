package com.comp691;

/**
 * This code belongs to
 * Student name: Avaneesh Kanshi
 * Student Id: 40273760
 * 
 * Dept. of Computer Science and Software Engineering
 * Concordia University
 * 
 * This is the code for Phase 1 of the project component of COMP 691: Online Algorithms and Competitive Analysis
 * Winter 2024
 */

/**
 * This class is to test out the functionalities of the code
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Testing Suite for Low Threshold values:\n\n");
        // Testing suite
        TestsLowThreshold testingSuiteLowThresh = new TestsLowThreshold();
        // Category 1: Normal Behavior
        testingSuiteLowThresh.test1();

        // Category 2: N = k+1
        testingSuiteLowThresh.test2();
        testingSuiteLowThresh.test3();
        testingSuiteLowThresh.test4();
        testingSuiteLowThresh.test5();

        // Category 3: N >> k
        testingSuiteLowThresh.test6();
        testingSuiteLowThresh.test7();
        testingSuiteLowThresh.test8();
        testingSuiteLowThresh.test9();

        System.out.println("\nTesting Suite for High Threshold values:\n\n");
        TestsHighThreshold testingSuiteHighThresh = new TestsHighThreshold();
        // Category 1: Normal Behavior
        testingSuiteHighThresh.test1();

        // Category 2: N = k+1
        testingSuiteHighThresh.test2();
        testingSuiteHighThresh.test3();
        testingSuiteHighThresh.test4();
        testingSuiteHighThresh.test5();

        // Category 3: N >> k
        testingSuiteHighThresh.test6();
        testingSuiteHighThresh.test7();
        testingSuiteHighThresh.test8();
        testingSuiteHighThresh.test9();

    }
}