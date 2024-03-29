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
     * 
     * @param k
     * @param pageRequestSequence
     * @param predictedHSeq
     * @param thr
     * @return
     */
    public int combinedAlg(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq, double thr) {
        // LRU Page faults
        int f1 = 0;

        // Blind Oracle Page faults
        int f2 = 0;

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
            // System.out.println("f1:" + f1 + " f2:" + f2);

            // Thought prcoess after discussion with denis
            // First process CA, then calculate LRU and BO

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
                f1 += 1;
                // Updates the most recently requested page
                LRU_cache.add(requestedPage);
                LRU_recentPageRequestMemory.addFirst(requestedPage);

                // BO also puts page into its cache
                f2 += 1;
                BO_cache.put(requestedPage, predictedHValue);

            }

            // For the requests i>=k+1
            else {
                // we need to update CA, F1 AND F2

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

                // compute f1 and f2
                // Compute F1
                // Least Recently Used Algorithm Implementation
                // Case: Page fault, CACHE MISS

                if (!LRU_cache.contains(requestedPage)) {
                    // The cache does not contain requested page, then we increment cache miss by 1
                    f1 += 1;

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

                // Compute F2
                if (BO_cache.containsKey(requestedPage)) {
                    BO_cache.put(requestedPage, predictedHValue);
                }
                //
                // Case 2: Page fault, CACHE MISS
                else {

                    // Increment overall page fault by 1
                    f2 += 1;

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
                if ((double) (f1) > (double) ((1 + thr) * f2) && ALG == CURRENT_ALGORITHM.LEAST_RECENTLY_USED) {
                    System.out.println("Switching to BO");
                    System.out.println("f1:" + f1 + " f2:" + f2);
                    ALG = CURRENT_ALGORITHM.BLIND_ORACLE;
                }

                // Case: f2 > (1+thr)*f1, ALG switches from Blind Oracle to LRU
                else if ((double) (f2) > (double) ((1 + thr) * f1) && ALG == CURRENT_ALGORITHM.BLIND_ORACLE) {
                    System.out.println("Switching to LRU");
                    System.out.println("f1:" + f1 + " f2:" + f2);
                    ALG = CURRENT_ALGORITHM.LEAST_RECENTLY_USED;
                }

            }

        }

        return r_pageFault;

    }
}
