package com.comp691;

import java.util.List;

/**
 * This class tests the functionality of Combined Algorithm, LRU and Blind
 * Oracle in different conditions for low threshold values. (thr < 0.5)
 */
public class TestsLowThreshold {
    // Threshold parameter for CA
    private final double thr = 0.1;

    /**
     * Test 1: This is a normal test case with k = 10 and N = 100 mainly for testing
     * the correct behaviour of the BlindOracle, LRU and Combined algorithm.
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 1: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 1: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 1: # of page faults incurred by Combined Algorithm: " + caPageFaults);
            System.out.println("Test 1: executed successfully!");
            return true;

        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 1: Has completed\n");
        }

    }

    /**
     * Test 2: This test case tests how the algorithms performs when N = k+1 with a
     * very large request sequence in the given scenario where the probability that
     * a request to a page existing in the cache is made is very low (e < 0.1)
     * the correct behaviour of the BlindOracle, LRU and CA algorithm.
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 2: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 2: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 2: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 3: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 3: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 3: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 4: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 4: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 4: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 5: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 5: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 5: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 6: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 6: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 6: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 7: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 7: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 7: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 8: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 8: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 8: # of page faults incurred by Combined Algorithm: " + caPageFaults);
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
            List<Integer> pageSequence = SequenceGenerator.generateRandomSequence(k, N, n, e);
            List<Integer> hSeq = SequenceGenerator.generateH(pageSequence);
            List<Integer> predictedhSeq = SequenceGenerator.addNoise(hSeq, tau, w);

            BlindOracle testObjectBO = new BlindOracle();
            int boPageFaults = testObjectBO.blindOracle(k, pageSequence, predictedhSeq);
            LeastRecentlyUsed testObjectLRU = new LeastRecentlyUsed();
            int lruPageFaults = testObjectLRU.leastRecentlyUsed(k, pageSequence);
            CombinedAlgorithm testObjectCA = new CombinedAlgorithm();
            int caPageFaults = testObjectCA.combinedAlg(k, pageSequence, predictedhSeq, thr);

            System.out.println("Test 9: # of page faults incurred by LRU: " + lruPageFaults);
            System.out.println("Test 9: # of page faults incurred by Blind Oracle: " + boPageFaults);
            System.out.println("Test 9: # of page faults incurred by Combined Algorithm: " + caPageFaults);
            System.out.println("Test 9: executed successfully!");
            return true;
        } catch (RuntimeException e) {
            return false;
        } finally {
            System.out.println("Test 9: Has completed\n");
        }

    }
}
