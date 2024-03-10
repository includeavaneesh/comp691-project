package com.comp691;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlindOracle {

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
        // Storing page requests p(i) where i <= k
        for (int i = 1; i <= k; i++) {
            r_pageRequestSequence.add(i);
            l_pageList.add(i);
        }

        for (int i = k + 1; i <= N; i++) {
            l_globalMemory.add(i);
        }
        // For page requests i>k

        int l_globalLength = N - k;
        // Storing page requests p(i) where i > k
        for (int i = k + 1; i <= n; i++) {
            int randomPageX = (int) (Math.random() * k) + 1;
            int randomPageY = (int) (Math.random() * l_globalLength) + 1;

            int x = l_pageList.get(randomPageX - 1);
            int y = l_globalMemory.get(randomPageY - 1);

            double randomEps = Math.random();

            if (randomEps < e) {

                // Add x to the page request sequence
                r_pageRequestSequence.add(x);

                // dont change globalMemory
            }

            else {

                // Add y to the page request sequence
                r_pageRequestSequence.add(y);

                // Global and local memory are updated by adding y into local memory
                l_pageList.remove(l_pageList.indexOf(x));
                l_pageList.add(y);
                l_globalMemory.remove(l_globalMemory.indexOf(y));
                l_globalMemory.add(x);

            }

        }
        System.out.println(r_pageRequestSequence);
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
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 1; i <= n; i++) {

            // Current page request whose h-value is to be generated
            int currentPage = pageRequestSequence.get(n - i);

            // Checks if the page request has been requested in the future or not,
            // if it is requested then store the min(j) where j > current request index
            if (map.containsKey(currentPage)) {

                // Sets the current h value as the nearest similar page request in the future
                hSequence[n - i] = map.get(currentPage);
                map.put(currentPage, n - i + 1);
            }

            // else h-value = n + 1
            else {

                // Sets the current h value as the n + 1 where n is the sequence length
                hSequence[n - i] = n + 1;
                map.put(currentPage, n - i + 1);
            }
        }

        return Arrays.asList(hSequence);
    }

    /**
     * This function generated a sequence of predicted values of h, denoted by h^
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
            if (randomTau < 1 - tau) {
                r_predictedHSequence.add(currentHValue);
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
            }
        }

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
        for (int i = 0; i < n; i++) {

            // The current page request and the corresponding predicted h value that are
            // being processed
            int currentPageRequest = pageRequestSequence.get(i);
            int predictedHValue = predictedHSeq.get(i);

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

                    int maxH = Integer.MIN_VALUE;
                    int pageToBeEvicted = -1;

                    // Finding the max predicted h value and corresponding page in the cache
                    for (Map.Entry<Integer, Integer> entry : cache.entrySet()) {
                        if (entry.getValue() >= maxH) {
                            maxH = entry.getValue();
                            pageToBeEvicted = entry.getKey();
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

    public static void main(String[] args) {
        // do sonme code
        int k = 9;
        List<Integer> pageSequence = generateRandomSequence(k, 100, 200, 0.1);
        List<Integer> hSeq = generateH(pageSequence);
        List<Integer> predictedhSeq = addNoise(hSeq, 0.9999, 6);
        System.out.println(pageSequence);
        System.out.println(hSeq);
        System.out.println(predictedhSeq);

        int pageFaults = blindOracle(k, pageSequence, predictedhSeq);
        int pageFaults2 = blindOracle(k, pageSequence, hSeq); // OPT
        System.out.println(pageFaults + ": " + pageFaults2);
    }
}