package com.comp691;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BlindOracle {

    /**
     * 
     * @param k Cache Size
     * @param N Memory Size
     * @param n Input length
     * @param e Amount of locality in a sequence to be generated
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
            // System.out.println(l_globalLength);
            // System.out.println(l_globalMemory.size());
            int x = l_pageList.get(randomPageX - 1);
            int y = l_globalMemory.get(randomPageY - 1);

            double randomEps = Math.random();

            if (randomEps < e) {
                r_pageRequestSequence.add(x);
                System.out.println("No change needed");
                // dont change globalMemory
            }

            else {
                r_pageRequestSequence.add(y);

                l_pageList.remove(l_pageList.indexOf(x));
                l_pageList.add(y);
                l_globalMemory.remove(l_globalMemory.indexOf(y));
                l_globalMemory.add(x);
                l_globalLength -= 1; // reduce global memory length by 1

            }

            // System.out.println("Local pages: " + l_pageList);
            // System.out.println("Global pages: " + l_globalMemory);
            // Output is different everytime this code is executed

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
     * @param p_pageRequestSequence Page request sequence
     * @return H value corresponding to each page in the request sequence
     */
    public static List<Integer> generateH(List<Integer> p_pageRequestSequence) {

        // Request sequence size
        int n = p_pageRequestSequence.size();

        // Initialize an empty array which will store the h-values.
        Integer[] hSequence = new Integer[n];

        // Temporary HashMap that responds if there exists a request where pi = pj
        // for j > i
        // Generating H-sequence is O(n) with the help of HashMap data structure
        // Note: 0-based index
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 1; i <= n; i++) {

            // Current page request whose h-value is to be generated
            int currentPage = p_pageRequestSequence.get(n - i);

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
     * @return
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
                r_predictedHSequence.add(randomPredictedH);
            }
        }

        return r_predictedHSequence;
    }

    public static void main(String[] args) {
        // do sonme code
        List<Integer> hSeq = generateH(generateRandomSequence(3, 10, 9, 0.5));
        System.out.println(hSeq);
        addNoise(hSeq, 0.000001, 3);
    }
}