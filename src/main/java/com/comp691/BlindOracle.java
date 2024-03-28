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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the implementation of Phase 1: Blind Oracle
 */
public class BlindOracle {

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
    public int blindOracle(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq) {

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
            int requestedPage = pageRequestSequence.get(index);
            int predictedHValue = predictedHSeq.get(index);

            // Case 1: No page fault, CACHE HIT
            if (cache.containsKey(requestedPage)) {
                cache.put(requestedPage, predictedHValue);
            }

            // Case 2: Page fault, CACHE MISS
            else {

                // Increment overall page fault by 1
                r_pageFaults += 1;

                // Subcase 1: If the cache has space for another page, then no need to evict a
                // page
                if (cache.size() < k && cache.size() >= 0) {
                    cache.put(requestedPage, predictedHValue);
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
                    cache.put(requestedPage, predictedHValue);
                }
            }
        }

        // Return the total number of page faults
        return r_pageFaults;

    }

}