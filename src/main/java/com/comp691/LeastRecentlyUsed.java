package com.comp691;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

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

/**
 * This is the implementation of Phase 2: Least Recently Used (LRU)
 */
public class LeastRecentlyUsed {

    /**
     * This function returns the number of page faults incurred by the cache when
     * LRU paging algorithm is employed. The algorithm works as follows:
     * 
     * Case 1: CACHE HIT
     * If the current page request exists in the cache, it does not incur a page
     * fault. Moreover, the recently requested page list is updated with the current
     * request.
     * 
     * Case 2: CACHE MISS
     * If the current page request does not exists in the cache, either there is an
     * empty space to accomodate another page or the cache is full and the least
     * recently used page is evicted
     * 
     * @param k                   Cache size
     * @param pageRequestSequence Sequence of page requests made
     * @return Page Faults incurred by LRU algorithm
     */
    public int leastRecentlyUsed(int k, List<Integer> pageRequestSequence) {

        // Implementing a hash set for cache to store requested pages
        HashSet<Integer> cache = new HashSet<>();
        // Implementing a linked queue to store the history of page request
        Deque<Integer> recentPageRequestMemory = new LinkedList<>();

        // Variable that stores the number of page faults
        int r_pageFaults = 0;

        // Traversing the page request sequence and employing the LRU algorithm
        for (int requestedPage : pageRequestSequence) {

            // Case 2: Page fault, CACHE MISS
            if (!cache.contains(requestedPage)) {
                // The cache does not contain requested page, then we increment cache miss by 1
                r_pageFaults++;

                // If the cache is full, we decide the least recently used page should be
                // evicted
                if (cache.size() == k) {
                    int removedPage = recentPageRequestMemory.removeLast();
                    cache.remove(removedPage);
                }

                // Updates the most recently requested page
                cache.add(requestedPage);
                recentPageRequestMemory.addFirst(requestedPage);
            }

            // Case 1: No page fault, CACHE HIT
            else {

                // This part maintains which page is the most recently requested
                recentPageRequestMemory.remove(requestedPage);
                recentPageRequestMemory.addFirst(requestedPage);
            }
        }

        // Return the total number of page faults
        return r_pageFaults;

    }
}
