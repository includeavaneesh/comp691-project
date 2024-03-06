package com.comp691;

import java.util.ArrayList;
import java.util.List;

public class BlindOracle {

    /**
     * 
     * @param k Cache Size
     * @param N Memory Size
     * @param n Input length
     * @param e Amount of locality in a sequence to be generated
     */
    public static ArrayList<Integer> generateRandomSequence(int k, int N, int n, double e) {
        List<Integer> r_pageRequest = new ArrayList<Integer>();
        List<Integer> l_pageList = new ArrayList<Integer>();
        List<Integer> l_globalMemory = new ArrayList<Integer>();

        // First k requests are to pages 1,2,...,k
        // Storing page requests p(i) where i <= k
        for (int i = 1; i <= k; i++) {
            r_pageRequest.add(i);
            l_pageList.add(i);
        }

        for (int i = k + 1; i <= N; i++) {
            l_globalMemory.add(i);
        }
        // For page requests i>k

        int l_globalLength = N - k;
        // Storing page requests p(i) where i > k
        for (int i = k+1; i <= n; i++) {
            int randomPageX = (int) (Math.random() * k) + 1;
            int randomPageY = (int) (Math.random() * l_globalLength) + 1;
            // System.out.println(l_globalLength);
            // System.out.println(l_globalMemory.size());
            int x = l_pageList.get(randomPageX - 1);
            int y = l_globalMemory.get(randomPageY - 1);

            double randomEps = Math.random();

            if (randomEps < e) {
                r_pageRequest.add(x);
                System.out.println("No change needed");
                // dont change globalMemory
            }

            else {
                r_pageRequest.add(y);

                l_pageList.remove(l_pageList.indexOf(x));
                l_pageList.add(y);
                l_globalMemory.remove(l_globalMemory.indexOf(y));
                l_globalLength -= 1; // reduce global memory length by 1

            }

            System.out.println("Local pages: " + l_pageList);
            System.out.println("Global pages: " + l_globalMemory);
            // Output is different everytime this code is executed

        }

        return (ArrayList<Integer>) r_pageRequest;

    }

    public static void main(String[] args) {
        // do sonme code
        
    }
}