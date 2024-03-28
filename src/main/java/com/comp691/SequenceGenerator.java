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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


class SequenceGenerator {
    /**
     * This function generates a random sequence of page requests by taking the
     * cache size (k), large/global memory (N), number of requests to be made (n)
     * and a threshold value which controls locality (e).
     * 
     * The first initial k pages are {1,2,...,k} and L is the local memory pool.
     * Following that, a new page request
     * is generated as follows:
     * 
     * x <- choose a page from the local memory pool wuith uniform randomness
     * y <- choose a page with uniform randomness from the the global memory pool
     * which does not contain pages from local pool.
     * 
     * With a probability of e, add x to the page request sequence
     * With the remainibg probability of 1-e, add y to the page request sequence.
     * Thereafter, update the local pool by eliminating x from it and adding y
     * 
     * @param k Cache Size
     * @param N Memory Size
     * @param n Input length
     * @param e Amount of locality in a sequence to be generated
     * @return List of random page requests
     */
    public static List<Integer> generateRandomSequence(int k, int N, int n, double e) {
        List<Integer> r_pageRequestSequence = new ArrayList<Integer>();
        List<Integer> l_pageList = new ArrayList<Integer>();
        List<Integer> l_globalMemory = new ArrayList<Integer>();

        // First k requests are to pages 1,2,...,k
        for (int index = 1; index <= k; index++) {
            // Storing page requests p(i) where i <= k
            r_pageRequestSequence.add(index);
            // Initializing local memory L
            l_pageList.add(index);
        }

        // Initializing the global memory N
        for (int index = k + 1; index <= N; index++) {
            l_globalMemory.add(index);
        }
        // For page requests i>k

        int l_globalLength = N - k;
        // Storing page requests p(i) where i > k
        for (int index = k + 1; index <= n; index++) {
            int randomPageX = (int) (Math.random() * k) + 1;
            int randomPageY = (int) (Math.random() * l_globalLength) + 1;

            int x = l_pageList.get(randomPageX - 1);
            int y = l_globalMemory.get(randomPageY - 1);

            // Generating a value between 0 and 1 with uniform probability to handle e
            double randomEps = Math.random();

            if (randomEps < e) {

                // Add x to the page request sequence
                r_pageRequestSequence.add(x);
                // Dont change global memory
            }

            else {

                // Add y to the page request sequence
                r_pageRequestSequence.add(y);

                // Global is updated by adding y into local memory
                l_pageList.remove(l_pageList.indexOf(x));
                l_pageList.add(y);
                l_globalMemory.remove(l_globalMemory.indexOf(y));
                l_globalMemory.add(x);

            }

        }
        return (ArrayList<Integer>) r_pageRequestSequence;

    }

    /**
     * This function generated the corresponding h-values for a request sequence
     * with the following function signature
     * h(i) = min(j) where j > i if p(i) equals p(j), else n + 1 where n <- request
     * sequence length
     * 
     * @param pageRequestSequence Page request sequence
     * @return H value corresponding to each page in the request sequence
     */
    public static List<Integer> generateH(List<Integer> pageRequestSequence) {

        // Request sequence size
        int n = pageRequestSequence.size();

        // Initialize an empty array which will store the h-values.
        Integer[] hSequence = new Integer[n];

        // Temporary HashMap that responds if there exists a request where pi = pj
        // for j > i
        // Generating H-sequence is O(n) with the help of HashMap data structure
        // Note: 0-based index
        HashMap<Integer, Integer> temporaryMap = new HashMap<Integer, Integer>();

        for (int index = 1; index <= n; index++) {

            // Current page request whose h-value is to be generated
            int currentPage = pageRequestSequence.get(n - index);

            // Checks if the page request has been requested in the future or not,
            // if it is requested then store the min(j) where j > current request index
            if (temporaryMap.containsKey(currentPage)) {

                // Sets the current h value as the nearest similar page request in the future
                hSequence[n - index] = temporaryMap.get(currentPage);
                temporaryMap.put(currentPage, n - index + 1);
            }

            // else h-value = n + 1
            else {

                // Sets the current h value as the n + 1 where n is the sequence length
                hSequence[n - index] = n + 1;
                temporaryMap.put(currentPage, n - index + 1);
            }
        }

        return Arrays.asList(hSequence);
    }

    /**
     * This function generates a sequence of predicted values of h, denoted by h^
     * based on h-sequence, and two noise parameters τ and w. Using the noise
     * parameters, the function basically adds noise to the generated h-sequence to
     * generate predicted h sequence
     * 
     * @param hSeq Sequence of h-values generated using the input page request
     *             sequence (refer BlindOracle.generateH())
     * @param tau  A noise parameter lies in the range (0,1)
     * @param w    A noise parameter, takes value from set of all natural
     *             numbers
     * @return Sequence of predicted H values
     */
    public static List<Integer> addNoise(List<Integer> hSeq, double tau, int w) {

        // Initialize an empty list of predicted h values
        List<Integer> r_predictedHSequence = new ArrayList<Integer>();
        double predictionError = 0;
        int n = hSeq.size();

        // Traverse each h value in the hSeq, to determine the predicted h using the
        // specific noise parameters τ and w
        // Note: 0-based index
        for (int i = 0; i < n; i++) {

            // Probability value to set h^
            double randomTau = Math.random();

            // Current h value
            int currentHValue = hSeq.get(i);

            // Sets the predicted h value as the current h value with probability 1-τ
            if (randomTau < (1 - tau)) {
                r_predictedHSequence.add(currentHValue);
                predictionError += 0;
            }

            // Sets the predicted h value with probability τ as a uniform number chosen
            // between l and l+w (inclusive) where l = max(i+1, current h - w/2)
            else {

                // Note i + 1 <- i + 2 as this is 0-based indexing
                int l = Math.max(i + 2, currentHValue - (int) Math.floor(w / 2));

                // Chooses a number between l and l+w at random
                int randomPredictedH = (int) (Math.random() * (l + w)) + l;

                // Add the predicted H value to the sequence
                r_predictedHSequence.add(randomPredictedH);

                predictionError += Math.abs(randomPredictedH - currentHValue);
            }
        }

        System.out.println("Prediction error: " + predictionError);
        return r_predictedHSequence;
    }

    

}
