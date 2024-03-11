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
import java.util.Map;

/**
 * This is the implementation of Phase 1: Blind Oracle
 */
class BlindOracle {
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

    /**
     * This function returns the number of page faults incurred by the cache when
     * Blind Oracle paging algorithm is employed. The algorithm works using
     * predicted h values calculated using BlindOracle.generateH and
     * BlindOracle.addNoise class functions.
     * 
     * Case 1: CACHE HIT
     * If the current page request exists in the cache, it does not incur a page
     * fault. However, the predicted h value of that page in the cache is to be
     * updated to the new h-value.
     * 
     * Case 2: CACHE MISS
     * If the current page request does not exists in the cache
     * 
     * Subcase 1: There is space for a new page to be inserted without evicting any
     * previous page from the cache (when cache size < k). The new page is simply
     * inserted with it's predicted h value.
     * 
     * Subcase 2: If the cache is full, then the page with the largest and latest
     * predicted h-value is evicted from the cache, and the new page is inserted
     * with it's corresponding predicted h-value
     * 
     * @param k                   Cache size
     * @param pageRequestSequence Sequence of page requests made
     * @param predictedHSeq       Sequence of predicted h value
     * @return Page Faults incurred by Blind Oracle algorithm
     */

    public static int blindOracle(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq) {

        // Variable that stores the number of page faults
        int r_pageFaults = 0;

        // Implementing a hash map for cache to store predicted h value with
        // corresponding page, page retrieval, updation is O(1)
        HashMap<Integer, Integer> cache = new HashMap<Integer, Integer>();

        // Size of the page request sequence
        int n = pageRequestSequence.size();

        // Traversing the page request sequence and employing the blind oracle algorithm
        for (int index = 0; index < n; index++) {

            // The current page request and the corresponding predicted h value that are
            // being processed
            int currentPageRequest = pageRequestSequence.get(index);
            int predictedHValue = predictedHSeq.get(index);

            // Case: No page fault, CACHE HIT
            if (cache.containsKey(currentPageRequest)) {
                cache.put(currentPageRequest, predictedHValue);
            }

            // Case 2: Page fault, CACHE MISS
            else {

                // Increment overall page fault by 1
                r_pageFaults += 1;

                // Subcase 1: If the cache has space for another page, then no need to evict a
                // page
                if (cache.size() < k && cache.size() >= 0) {
                    cache.put(currentPageRequest, predictedHValue);
                }

                // Subcase 2: If the cache is full, then we evict the page with highest
                // predicted h value
                else {

                    // Variable to store maximum predicted H value when cache miss occurs
                    int maxH = Integer.MIN_VALUE;

                    // Variable to store the page to be evicted when cache miss occurs
                    int pageToBeEvicted = -1;

                    // Finding the max predicted h value and corresponding page in the cache
                    for (Map.Entry<Integer, Integer> cacheEntry : cache.entrySet()) {
                        if (cacheEntry.getValue() >= maxH) {
                            maxH = cacheEntry.getValue();
                            pageToBeEvicted = cacheEntry.getKey();
                        }
                    }

                    // Evicting the page with the highest h value
                    cache.remove(pageToBeEvicted);

                    // Inserting a new page in it's place
                    cache.put(currentPageRequest, predictedHValue);
                }
            }
        }

        // Return the total number of page faults
        return r_pageFaults;

    }

    /**
     * Test 1: This is a normal test case with k = 10 and N = 100 mainly for testing
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test1() {

        try {
            // k, N, n are the cache size, global size and request sequence length,
            // respectively.
            int k = 10;
            int N = 100;
            int n = 10000;

            // Extra parameters for locality and noise calculations
            double e = 0.4;
            double tau = 0.6;
            int w = 6;

            System.out.println("Test 1: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);

            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 1: # of page faults incurred " + pageFaults);
            System.out.println("Test 1: executed successfully!");
            return true;

        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 1: Has completed\n");
        }

    }

    /**
     * Test 2: This test case tests how the algorithm performs when N = k+1 with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very low (e < 0.1)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is high, the deviation from the
     * actual h sequence should be large.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test2() {

        try {
            int k = 10;
            int N = k + 1;
            int n = 100000;
            double e = 0.001;
            double tau = 0.9;
            int w = 10;

            System.out.println("Test 2: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 2: # of page faults incurred " + pageFaults);
            System.out.println("Test 2: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 2: Has completed\n");
        }

    }

    /**
     * Test 3: This test case tests how the algorithm performs when N = k+1 with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very high (e > 0.9)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is high, the deviation from the
     * actual h sequence should be large.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test3() {

        try {
            int k = 10;
            int N = k + 1;
            int n = 100000;
            double e = 0.999;
            double tau = 0.9;
            int w = 10;

            System.out.println("Test 3: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 3: # of page faults incurred " + pageFaults);
            System.out.println("Test 3: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 3: Has completed\n");
        }

    }

    /**
     * Test 4: This test case tests how the algorithm performs when N = k+1 with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very low (e < 0.1)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is low, the deviation from the
     * actual h sequence should be small.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test4() {

        try {
            int k = 10;
            int N = k + 1;
            int n = 100000;
            double e = 0.001;
            double tau = 0.1;
            int w = 10;

            System.out.println("Test 4: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 4: # of page faults incurred " + pageFaults);
            System.out.println("Test 4: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 4: Has completed\n");
        }

    }

    /**
     * Test 5: This test case tests how the algorithm performs when N = k+1 with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very high (e > 0.9)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is low, the deviation from the
     * actual h sequence should be small.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test5() {

        try {
            int k = 10;
            int N = k + 1;
            int n = 100000;
            double e = 0.999;
            double tau = 0.1;
            int w = 10;

            System.out.println("Test 5: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 5: # of page faults incurred " + pageFaults);
            System.out.println("Test 5: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 5: Has completed\n");
        }

    }

    /**
     * Test 6: This test case tests how the algorithm performs when N >> k with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very low (e < 0.1)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is high, the deviation from the
     * actual h sequence should be large.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test6() {

        try {
            int k = 10;
            int N = 1000000;
            int n = 10000;
            double e = 0.001;
            double tau = 0.9;
            int w = 10;

            System.out.println("Test 6: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 6: # of page faults incurred " + pageFaults);
            System.out.println("Test 6: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 6: Has completed\n");
        }

    }

    /**
     * Test 7: This test case tests how the algorithm performs when N >> k with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very high (e > 0.9)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is high, the deviation from the
     * actual h sequence should be large.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test7() {

        try {
            int k = 10;
            int N = 1000000;
            int n = 10000;
            double e = 0.999;
            double tau = 0.9;
            int w = 10;

            System.out.println("Test 7: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 7: # of page faults incurred " + pageFaults);
            System.out.println("Test 7: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 7: Has completed\n");
        }

    }

    /**
     * Test 4: This test case tests how the algorithm performs when N >> k with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very low (e < 0.1)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is low, the deviation from the
     * actual h sequence should be small.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test8() {

        try {
            int k = 10;
            int N = 1000000;
            int n = 10000;
            double e = 0.001;
            double tau = 0.1;
            int w = 10;

            System.out.println("Test 8: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 8: # of page faults incurred " + pageFaults);
            System.out.println("Test 8: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 8: Has completed\n");
        }

    }

    /**
     * Test 9: This test case tests how the algorithm performs when N >> k with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very high (e > 0.9)
     * the correct behaviour of the BlindOracle algorithm.
     * 
     * Predictive h sequence: As the tau value is low, the deviation from the
     * actual h sequence should be small.
     * 
     * @return True if the test is successful, else false.
     */
    public boolean test9() {

        try {
            int k = 10;
            int N = 1000000;
            int n = 10000;
            double e = 0.999;
            double tau = 0.1;
            int w = 10;

            System.out.println("Test 9: Executing test ...");
            List<Integer> pageSequence = generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = generateH(pageSequence);
            List<Integer> predictedhSeq = addNoise(hSeq, tau, w);
            int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
            System.out.println("Test 9: # of page faults incurred " + pageFaults);
            System.out.println("Test 9: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 9: Has completed\n");
        }

    }

}

/**
 * This class is to test out the functionalities of the code
 */
public class Main {

    public static void main(String[] args) {

        // Testing suite

        // Category 1: Normal Behavior
        new BlindOracle().test1();

        // Category 2: N = k+1
        new BlindOracle().test2();
        new BlindOracle().test3();
        new BlindOracle().test4();
        new BlindOracle().test5();

        // Category 3: N >> k
        new BlindOracle().test6();
        new BlindOracle().test7();
        new BlindOracle().test8();
        new BlindOracle().test9();

    }
}