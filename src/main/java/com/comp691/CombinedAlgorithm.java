package com.comp691;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This code belongs to
 * Student name: Avaneesh Kanshi
 * Student Id: 40273760
 * 
 * Dept. of Computer Science and Software Engineering
 * Concordia University
 * 
 * This is the code for Phase 2 of the project component of COMP 691: Online Algorithms and Competitive Analysis
 * Winter 2024
 */

import java.util.List;
import java.util.Map;

enum CURRENT_ALGORITHM {
    BLIND_ORACLE,
    LEAST_RECENTLY_USED
}

/**
 * This is the implementation of Phase 2: Combined Algorithm
 */
public class CombinedAlgorithm {

    /**
     * This function is the implementation for Combined Algorithm. It takes in the
     * cache size, input request sequence, predictive locality sequence and a
     * threshold parameter. This algorithm intuitively runs Blind Oracle and Least
     * Recently Used algorithm in parallel. For the first initial k requests, it
     * purely runs LRU algorithm. Then for the page request i > k, it switches to
     * Blind Oracle or LRU, to whichever one has incurred the least amount of page
     * faults, determined by the threshold parameter.
     * 
     * 
     * @param k                   Cache Size
     * @param pageRequestSequence Sequence of page requests made
     * @param predictedHSeq       Sequence of predicted h value
     * @param thr                 Threshold Value
     * @return Page Faults incurred by Combined algorithm
     */
    public int combinedAlg(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq, double thr) {
        // LRU Page faults
        int LRU_pageFaults = 0;

        // Blind Oracle Page faults
        int BO_pageFaults = 0;

        // Combined Algo page faults
        int r_pageFault = 0;

        // Implementing a hash set for LRU cache to store requested pages
        HashSet<Integer> LRU_cache = new HashSet<>();
        // Implementing a hash map for Blind Oracle cache to store requested pages
        HashMap<Integer, Integer> BO_cache = new HashMap<Integer, Integer>();

        // Implementing a hash set for LRU cache to store requested pages
        HashSet<Integer> CombinedALG_cache = new HashSet<>();

        // Implementing a linked queue to store the history of page request for LRU
        Deque<Integer> LRU_recentPageRequestMemory = new LinkedList<>();
        int n = pageRequestSequence.size();

        // This enum variable stores the current algorithm being used, it initally
        // starts with LRU
        CURRENT_ALGORITHM ALG = CURRENT_ALGORITHM.LEAST_RECENTLY_USED;

        for (int index = 0; index < n; index++) {

            // According to clarification, first Combined Algorithm is executed with set
            // algorithm, then makes the switch depending on the page faults incurred so far
            // by LRU and Blind Oracle

            // So, for the purpose of this algorithm, for the first k requests, CA maintains
            // LRU implementation
            int requestedPage = pageRequestSequence.get(index);
            int predictedHValue = predictedHSeq.get(index);
            int removedPage = -1;

            if (index < k) {

                // The first k requests are to the pages {1,2,3...k}
                CombinedALG_cache.add(requestedPage);
                r_pageFault += 1;

                // LRU also puts page into its cache
                LRU_pageFaults += 1;
                // Updates the most recently requested page
                LRU_cache.add(requestedPage);
                LRU_recentPageRequestMemory.addFirst(requestedPage);

                // BO also puts page into its cache
                BO_pageFaults += 1;
                BO_cache.put(requestedPage, predictedHValue);

            }

            // For the requests i>=k+1
            else {
                // we need to update CA, LRU page fault AND Blind Oracle page fault

                // IF CA IS LRU
                if (ALG == CURRENT_ALGORITHM.LEAST_RECENTLY_USED) {
                    if (!LRU_cache.contains(requestedPage)) {
                        r_pageFault += 1;
                    }
                    CombinedALG_cache = new HashSet<>(LRU_cache);
                }
                // else CA IS Blind Oracle
                else {

                    if (!BO_cache.containsKey(requestedPage)) {
                        r_pageFault += 1;
                    }
                    CombinedALG_cache = new HashSet<>(BO_cache.keySet());
                }

                // compute LRU faults and BO faults

                // Least Recently Used Algorithm Implementation
                // Case: Page fault, CACHE MISS
                if (!LRU_cache.contains(requestedPage)) {
                    // The cache does not contain requested page, then we increment cache miss by 1
                    LRU_pageFaults += 1;

                    // If the cache is full, we decide the least recently used page should be
                    // evicted
                    if (LRU_cache.size() == k) {
                        removedPage = LRU_recentPageRequestMemory.removeLast();
                        LRU_cache.remove(removedPage);
                    }

                    // Updates the most recently requested page
                    LRU_cache.add(requestedPage);
                    LRU_recentPageRequestMemory.addFirst(requestedPage);
                }

                // Case: No page fault, CACHE HIT
                else {

                    // This part maintains which page is the most recently requested
                    LRU_recentPageRequestMemory.remove(requestedPage);
                    LRU_recentPageRequestMemory.addFirst(requestedPage);
                }

                // Compute BO page faults
                if (BO_cache.containsKey(requestedPage)) {
                    BO_cache.put(requestedPage, predictedHValue);
                }
                //
                // Case 2: Page fault, CACHE MISS
                else {

                    // Increment overall page fault by 1
                    BO_pageFaults += 1;

                    // Subcase 1: If the cache has space for another page, then no need to evict a
                    // page
                    if (BO_cache.size() < k && BO_cache.size() >= 0) {
                        BO_cache.put(requestedPage, predictedHValue);
                    }

                    // Subcase 2: If the cache is full, then we evict the page with highest
                    // predicted h value
                    else {

                        // Variable to store maximum predicted H value when cache miss occurs
                        int maxH = Integer.MIN_VALUE;

                        // Variable to store the page to be evicted when cache miss occurs
                        // int pageToBeEvicted = -1;

                        // Finding the max predicted h value and corresponding page in the cache
                        for (Map.Entry<Integer, Integer> cacheEntry : BO_cache.entrySet()) {
                            if (cacheEntry.getValue() >= maxH) {
                                maxH = cacheEntry.getValue();
                                removedPage = cacheEntry.getKey();
                            }
                        }

                        // Evicting the page with the highest h value
                        BO_cache.remove(removedPage);

                        // Inserting a new page in it's place
                        BO_cache.put(requestedPage, predictedHValue);
                    }
                }

                // Case: f1 > (1+thr)*f2, ALG switches from LRU to Blind Oracle
                if ((double) (LRU_pageFaults) > (double) ((1 + thr) * BO_pageFaults)
                        && ALG == CURRENT_ALGORITHM.LEAST_RECENTLY_USED) {
                    ALG = CURRENT_ALGORITHM.BLIND_ORACLE;
                }

                // Case: f2 > (1+thr)*f1, ALG switches from Blind Oracle to LRU
                else if ((double) (BO_pageFaults) > (double) ((1 + thr) * LRU_pageFaults)
                        && ALG == CURRENT_ALGORITHM.BLIND_ORACLE) {
                    ALG = CURRENT_ALGORITHM.LEAST_RECENTLY_USED;
                }

            }

        }

        return r_pageFault;

    }
}
