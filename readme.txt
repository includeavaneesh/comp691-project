Author:
Student Name: Avaneesh Kanshi
Student ID: 40273760
Contact E-mail: avaneeshkanshi@gmail.com, avaneesh.kanshi@live.concordia.ca

Github Repository: https://github.com/includeavaneesh/comp691-project.git

CONTENTS:
1. About the Project
2. Functionalities
    -> generateRandomSequence
    -> generateH
    -> addNoise
    -> blindOracle (Blind Oracle)
    -> leastRecentlyUsed (LeastRecently Used)
    -> combinedAlg (Combined Algorithm)
3. Testing
    -> Low Threshold Values
        > Category: N > k
        > Category: N = k + 1
        > Category: N >> k
    -> High Threshold Values
        > Category: N > k
        > Category: N = k + 1
        > Category: N >> k
4. How to Execute the program
    -> Requirements  
    -> How to execute the script
5. Experimental Evaluation
6. Sample Outputs
    -> Sample 1
    -> Sample 2


1. About the project

The current state of the project implements a Blind Oracle algorithm for paging using locality of reference approach, 
the traditional Least Recently Used and a new algorithm called Combined Algorithm (Uses both LRU and Blind Oracle algorithm). 

In case of the optimal algorithm, the algorithm performs using the furthest in the future approach meaning that the 
page that is requested further in the future is evicted from the cache. To elaborate, the algorithm basically has the 
knowledge of when the next time the current page will be requested again in the future again. This knowledge is stored
in a list (h). 

The Blind Oracle is quite similar to the OPT (Furthest in the future) algorithm. It has a predictive knowledge
of when the next time the current page will be requested again in the future. This is calculated using some machine learning
model trained on previous data. When a page is requested, the algorithm looks for it in the cache. If there is such page in the
cache, then a page hit has occured. Moreover, each page in cache is updated with the most current predicted h-values. If such
page is not found in the cache, a cache miss occurs. The algorithm either finds an empty space and allocates it to the requested
page, or the page with the highest predicted h-value is evicted and the new page is put in place. 

Least Recently Used algorithm works on a similar principle to Furthest in Future, but rather evicts the page which has not been
recently requested.

The Combined Algorithm can be seen as an algorithm which runs both LRU and Blind Oracle at the same time. However, this algorithm
behaves as either LRU or Blind Oracle at a given point. Based on the page faults incurred by LRU and Blind Oracle so far, the Combined
algorithm decides whether to switch from LRU to Blind Oracle (or vice versa), or not. This is determined by a threshold parameter.

2. Functionalities

The following functionalities are implemented throughout different classes:

+-----------------------------------------------------------------------------------+
| generateRandomSequence(int k, int N, int n, double e)                             |
+-----------------------------------------------------------------------------------+
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

+------------------------------------------------------------------------------------+
| generateH(List<Integer> pageRequestSequence)                                       |
+------------------------------------------------------------------------------------+
     * This function generated the corresponding h-values for a request sequence
     * with the following function signature
     * h(i) = min(j) where j > i if p(i) equals p(j), else n + 1 where n <- request
     sequence length

+------------------------------------------------------------------------------------+
| addNoise(List<Integer> hSeq, double tau, int w)                                    |
+------------------------------------------------------------------------------------+
     * This function generates a sequence of predicted values of h, denoted by h^
     * based on h-sequence, and two noise parameters τ and w. Using the noise
     * parameters, the function basically adds noise to the generated h-sequence to
     * generate predicted h sequence

+------------------------------------------------------------------------------------+
| blindOracle(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq) |
+------------------------------------------------------------------------------------+
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

+------------------------------------------------------------------------------------+
| leastRecentlyUsed(int k, List<Integer> pageRequestSequence)                        |
+------------------------------------------------------------------------------------+
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

+------------------------------------------------------------------------------------------------+
| combinedAlg(int k, List<Integer> pageRequestSequence, List<Integer> predictedHSeq, double thr) |
+------------------------------------------------------------------------------------------------+
    * This function is the implementation for Combined Algorithm. It takes in the
     * cache size, input request sequence, predictive locality sequence and a
     * threshold parameter. This algorithm intuitively runs Blind Oracle and Least
     * Recently Used algorithm in parallel. For the first initial k requests, it
     * purely runs LRU algorithm. Then for the page request i > k, it switches to
     * Blind Oracle or LRU, to whichever one has incurred the least amount of page
     * faults, determined by the threshold parameter.
     *
     * Refer: BlindOracle.blindOracle() and LeastRecentlyUsed.leastRecentlyUsed()

3. Testing

The functionalities of the blind oracle algorithm, LRU and Combined algorithm are tested on several edge cases and normal cases to 
evaluate and determine the correctness of the behavior of the algorithm. The test cases can be divided into 3 categories of N = k + 1, 
N >> k and N > k. Moreover, each of the following cases are tested with low and high threshold parameters. The categories and different 
tests are discussed in detail below:

Note: It must be noted that each test is performed on different request sequence to maintain average performance throughout, as
comparing them on basis of same sequence could be biased to few scenarios performing the best. However, more detailed analysis
is required to evaluate the performance of the Blind oracle algorithm, LRU and Combined Algorithm.

-> Low Threshold Values (thr is kept 0.1 for testing purposes)

* Category: N > k
+------------------------------------------------------------------------------------+
| Test 1: test1()                                                                    |
+------------------------------------------------------------------------------------+
    This test cases is to check the normal behavior of the algorithms under average
    circumstances. With k being 10, and N being 100. The request sequence length is set
    at 10000.

    The probability of requesting a page in future that already is in the local cache is
    medium and the deviation in the predicted h-value is average as well. The BO algorithm
    in this case is expected to perform worse than the OPT. The LRU should perform slightly
    worse than Blind Oracle. As the thr value is low, the combined algorithm must perform
    somewhere between BO and LRU.


* Category: N = k + 1
+------------------------------------------------------------------------------------+
| Test 2: test2()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The BO algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the BO algorithm is expected to incur the highest
    number of page faults among all the scenarios. However, the LRU is not affected by the
    predicted h-values. Therefore, LRU performs slightly better than the BO algorithm. Moreover,
    as there is only a slight improvement in the number of page faults by LRU than BO, the page
    faults incurred by CA will be somewhere between the 2 algorithms.

+------------------------------------------------------------------------------------+
| Test 3: test3()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is high. The algorithm might
    incur a page fault in most requests, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test2().

    LRU in this case, expectedly performs way better than BO. Due to this reason, the
    combined algorithm executes LRU algorithm most of the time.

+------------------------------------------------------------------------------------+
| Test 4: test4()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is low. The algorithm is expected
    to incur page faults on most number of requests, as the new page (not present in the local
    cache) is requested rarely. However, the tau value is low, the prediction error (can be
    observed in the sample outputs) is low. In this scenario, the algorithm has a better idea
    of what the future inputs could be.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test2().

    As BO approaches OPT, LRU tends to performs worse than BO. Due to this reason, the
    combined algorithm executes Blind Oracle algorithm most of the time.

+------------------------------------------------------------------------------------+
| Test 5: test5()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the BEST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is low. The algorithm is expected
    to incur least number page faults on the requests, as the new page requested has a high
    probability of already being available in the cache. Moreover, the tau value is low, the 
    prediction error (can be observed in the sample outputs) is low. In this scenario, the 
    algorithm has a better idea of what the future inputs could be performing similar to OPT.

    Therefore, given the above scenario, the algorithm is expected to leasr number of page 
    faults in this category of tests.

    In this case, LRU performs almost similar to BO, as the future page requested has a high
    probability of already being available in the cache. Therefore, combined algorithm shall
    perform the same as LRU and BO.


* Category: N >> k
+------------------------------------------------------------------------------------+
| Test 6: test6()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithms when the global memory size
    is significantly large than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The BO algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to incur the highest
    number of page faults among all the scenarios. Moreover, it could incure more page
    faults than test2(), as there are more variability of the pages to be requested
    as N >> k. 

    In this case, LRU performs similar to BO, and as the values are close, the Combined algorithm
    tends to stick LRU algorithm.

+------------------------------------------------------------------------------------+
| Test 7: test7()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is high. The BO algorithm might
    incur a page fault in most requests, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test6(), however performing worse than OPT as it
    is unable to predict future pages accurately.

    In this case, LRU performs worse than BO by a noticable difference. Therefore, combined
    algorithm shall switch to Blind Oracle as soon as possible. Combined algorithm in this case
    will incur similar page faults to BO.

+------------------------------------------------------------------------------------+
| Test 8: test8()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is low. The algorithms are expected
    to incur page faults on most number of requests, as the new page (not present in the local
    cache) is requested rarely. However, the tau value is low, the prediction error (can be
    observed in the sample outputs) is low. In this scenario, the algorithm has a better idea
    of what the future inputs could be.

    Therefore, given the above scenario, the algorithms would perform similar to that of
    test6().

+------------------------------------------------------------------------------------+
| Test 9: test9()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the BEST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithms when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is low. The BO algorithm is expected
    to incur least number page faults on the requests, as the new page requested has a high
    probability of already being available in the cache. Moreover, the tau value is low, the 
    prediction error (can be observed in the sample outputs) is low. In this scenario, the 
    algorithm has a better idea of what the future inputs could be performing similar to OPT.

    Therefore, given the above scenario, the algorithm is expected to least number of page 
    faults. Moreover, in this case, LRU performs worse than BO by a noticable difference. Therefore, combined
    algorithm shall switch to Blind Oracle as soon as possible. Combined algorithm in this case
    will incur similar page faults to BO.


------------------------------------------------------------------------------------------------
-> High Threshold Values (thr is kept 0.8 for testing purposes)

* Category: N > k
+------------------------------------------------------------------------------------+
| Test 1: test1()                                                                    |
+------------------------------------------------------------------------------------+
    This test cases is to check the normal behavior of the algorithms under average
    circumstances. With k being 10, and N being 100. The request sequence length is set
    at 10000.

    The probability of requesting a page in future that already is in the local cache is
    medium and the deviation in the predicted h-value is average as well. The BO algorithm
    in this case is expected to perform worse than the OPT. The LRU should perform slightly
    worse than Blind Oracle. As the thr value is low, the combined algorithm must perform
    similar to LRU, as the difference between the page faults is small.


* Category: N = k + 1
+------------------------------------------------------------------------------------+
| Test 2: test2()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The BO algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the BO algorithm is expected to incur the highest
    number of page faults among all the scenarios. However, the LRU is not affected by the
    predicted h-values. Therefore, LRU performs slightly better than the BO algorithm. Moreover,
    as there is only a slight improvement in the number of page faults by LRU than BO, the page
    faults incurred by CA will remain equal to LRU, as the Combined algorithm is expected to run
    LRU most of the time.

+------------------------------------------------------------------------------------+
| Test 3: test3()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is high. The algorithm might
    incur a page fault in most requests, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test2().

    LRU in this case, expectedly performs way better than BO. As the difference is significantly
    large, it is easy for combined algorithm to remain with LRU. Due to this reason, the
    combined algorithm executes LRU algorithm most of the time.

+------------------------------------------------------------------------------------+
| Test 4: test4()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is low. The algorithms is expected
    to incur page faults on most number of requests, as the new page (not present in the local
    cache) is requested rarely. However, the tau value is low, the prediction error (can be
    observed in the sample outputs) is low. In this scenario, the algorithm has a better idea
    of what the future inputs could be.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test2().

    As BO approaches OPT, LRU tends to performs worse than BO. As the difference is significantly
    large, it is easy for combined algorithm to switch from LRU to BO.

+------------------------------------------------------------------------------------+
| Test 5: test5()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the BEST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is low. The algorithm is expected
    to incur least number page faults on the requests, as the new page requested has a high
    probability of already being available in the cache. Moreover, the tau value is low, the 
    prediction error (can be observed in the sample outputs) is low. In this scenario, the 
    algorithm has a better idea of what the future inputs could be performing similar to OPT.

    Therefore, given the above scenario, the algorithm is expected to leasr number of page 
    faults in this category of tests.

    In this case, LRU performs almost similar to BO, as the future page requested has a high
    probability of already being available in the cache. Therefore, combined algorithm shall
    perform the same as LRU and BO.


* Category: N >> k
+------------------------------------------------------------------------------------+
| Test 6: test6()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithms when the global memory size
    is significantly large than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The BO algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to incur the highest
    number of page faults among all the scenarios. Moreover, it could incure more page
    faults than test2(), as there are more variability of the pages to be requested
    as N >> k. 

    In this case, LRU performs similar to BO, and as the values are close, the Combined algorithm
    tends to stick LRU algorithm.

+------------------------------------------------------------------------------------+
| Test 7: test7()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is high. The BO algorithm might
    incur a page fault in most requests, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test6(), however performing worse than OPT as it
    is unable to predict future pages accurately.

    In this case, LRU performs better than BO by a noticable difference. Therefore, combined
    algorithm will not switch to Blind Oracle. Combined algorithm in this case
    will incur similar page faults to LRU.

+------------------------------------------------------------------------------------+
| Test 8: test8()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is low. The algorithms are expected
    to incur page faults on most number of requests, as the new page (not present in the local
    cache) is requested rarely. However, the tau value is low, the prediction error (can be
    observed in the sample outputs) is low. In this scenario, the algorithm has a better idea
    of what the future inputs could be.

    Therefore, given the above scenario, the algorithms would perform similar to that of
    test6(). Moreover, combined algorithm should incur similar page faults to LRU.

+------------------------------------------------------------------------------------+
| Test 9: test9()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the BEST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithms when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is low. The BO algorithm is expected
    to incur least number page faults on the requests, as the new page requested has a high
    probability of already being available in the cache. Moreover, the tau value is low, the 
    prediction error (can be observed in the sample outputs) is low. In this scenario, the 
    algorithm has a better idea of what the future inputs could be performing similar to OPT.

    Therefore, given the above scenario, the algorithm is expected to least number of page 
    faults. Moreover, in this case, LRU performs worse than BO by a noticable difference. Therefore, combined
    algorithm wont switch to Blind Oracle as soon as possible. Combined algorithm in this case
    will incur similar page faults to LRU.

Conclusion from testing:

-> If the thr value is low, then even small differences in the page faults of Blind Oracle and LRU, can
    let combined algorithm switch rapidly. However, if the difference is large, the combined algorithm
    tends to stick with the algorithm which makes the lowest number of page faults.
    
-> If the thr value is large, the algorithm does not change for small differences in the page faults.
    However, it can have a slight possibility to switch if the difference is significantly large.

4. How to Execute the program

+------------------------------------------------------------------------------------+
| Requirements                                                                       |
+------------------------------------------------------------------------------------+
     * The program can work with JDK version 20.0.2 and higher.
     * Java Version 17.0 is required for executing the program through Maven.

+------------------------------------------------------------------------------------+
| How to execute the script                                                          |
+------------------------------------------------------------------------------------+
Note: Ensure, Matplotlib version 2.0 or higher is already installed for graphs. To generate the graphs, you will need to 
run the project using Maven.

    * Simple JVM Command (For windows):
        Step 1: Go into the package's root directory "cd src\main\java"
        Step 2: Compile the java classes using "javac com\comp691\Main.java"
        Step 3: To run the script, run the main class using the following command "java com.comp691.Main"

    * Using Maven (Version required: Apache Maven 3.9.6 https://maven.apache.org/download.cgi) 
        Step 1: Build the application using from the project's root directory"mvn clean package"
        Step 2: Locate the .jar file, usually found in the /target folder. You can use the following command "cd target"
        Step 3: Run the .jar file using "java -jar project-1.0-SNAPSHOT.jar"

5. Experimental Evaluations

This section deals on how the graphs for the experiments were generated. The graphs are generated using a extended matplotlib
dependency which extends python matplotlib library to Java. The graphs are generated by using average results computed over 10,000
trials (a single bacth) for varying parameters. 

Initially, two regimes have been defined as follows:

The performance mainly depended upon the values of e and tau.
-> OPT performs significantly better than BlindOracle and BlindOracle performs significantly better than LRU
    e = 0.01, tau = 0.99
-> OPT performs significantly better than LRU and LRU performs significantly better than BlindOracle
    e = 0.99, tau = 0.99

As e increases, LRU starts performing better than Blind Oracle and as tau decreases Blind Oracle performs similar to OPT.

The following graphs have been generated as follows which test the dependencies by varying parameters:
1 <= k <= 25, N = 5*k
    1. Dependence on k (Regime 1)
    2. Dependence on k (Regime 2)

k = 5, N = 200, 5 <= w <= 100
    3. Dependence on w (Regime 1)
    4. Dependence on w (Regime 2)

k = 5, N = 200, w = 25
    5. Dependence on e (Low tau)
    6. Dependence on e (High tau)

k = 5, N = 200, w = 25
    5. Dependence on tau (Low e)
    6. Dependence on tau (High e)

To generate the graphs, you will need to run the main function in Experimentation/ExperimentationGraph.java. The graphs
should be generated in comp691/resources/results folder.

6. Sample Outputs

+------------------------------------------------------------------------------------+
| Sample 1                                                                           |
+------------------------------------------------------------------------------------+

Testing Suite for Low Threshold values:


Test 1: Executing test ...
Prediction error: 1.5431161E7
Test 1: # of page faults incurred by LRU: 6960
Test 1: # of page faults incurred by Blind Oracle: 6755
Test 1: # of page faults incurred by Combined Algorithm: 6757
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.256485849E9
Test 2: # of page faults incurred by LRU: 10077
Test 2: # of page faults incurred by Blind Oracle: 10173
Test 2: # of page faults incurred by Combined Algorithm: 10175
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.244911553E9
Test 3: # of page faults incurred by LRU: 310
Test 3: # of page faults incurred by Blind Oracle: 4441
Test 3: # of page faults incurred by Combined Algorithm: 315
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.47080412E8
Test 4: # of page faults incurred by LRU: 10247
Test 4: # of page faults incurred by Blind Oracle: 5607
Test 4: # of page faults incurred by Combined Algorithm: 5609
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.46107022E8
Test 5: # of page faults incurred by LRU: 290
Test 5: # of page faults incurred by Blind Oracle: 216
Test 5: # of page faults incurred by Combined Algorithm: 218
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.4771277E7
Test 6: # of page faults incurred by LRU: 9993
Test 6: # of page faults incurred by Blind Oracle: 9965
Test 6: # of page faults incurred by Combined Algorithm: 9993
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2674015E7
Test 7: # of page faults incurred by LRU: 37
Test 7: # of page faults incurred by Blind Oracle: 19
Test 7: # of page faults incurred by Combined Algorithm: 21
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 4852717.0
Test 8: # of page faults incurred by LRU: 9996
Test 8: # of page faults incurred by Blind Oracle: 9967
Test 8: # of page faults incurred by Combined Algorithm: 9996
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2575378.0
Test 9: # of page faults incurred by LRU: 30
Test 9: # of page faults incurred by Blind Oracle: 22
Test 9: # of page faults incurred by Combined Algorithm: 24
Test 9: executed successfully!
Test 9: Has completed


Testing Suite for High Threshold values:


Test 1: Executing test ...
Prediction error: 1.5098057E7
Test 1: # of page faults incurred by LRU: 6950
Test 1: # of page faults incurred by Blind Oracle: 6703
Test 1: # of page faults incurred by Combined Algorithm: 6950
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.245724581E9
Test 2: # of page faults incurred by LRU: 10094
Test 2: # of page faults incurred by Blind Oracle: 9905
Test 2: # of page faults incurred by Combined Algorithm: 10094
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.251944729E9
Test 3: # of page faults incurred by LRU: 259
Test 3: # of page faults incurred by Blind Oracle: 4321
Test 3: # of page faults incurred by Combined Algorithm: 259
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.56649399E8
Test 4: # of page faults incurred by LRU: 9935
Test 4: # of page faults incurred by Blind Oracle: 5693
Test 4: # of page faults incurred by Combined Algorithm: 5710
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.49960482E8
Test 5: # of page faults incurred by LRU: 296
Test 5: # of page faults incurred by Blind Oracle: 205
Test 5: # of page faults incurred by Combined Algorithm: 296
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.5235861E7
Test 6: # of page faults incurred by LRU: 9995
Test 6: # of page faults incurred by Blind Oracle: 9977
Test 6: # of page faults incurred by Combined Algorithm: 9995
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2342389E7
Test 7: # of page faults incurred by LRU: 46
Test 7: # of page faults incurred by Blind Oracle: 30
Test 7: # of page faults incurred by Combined Algorithm: 44
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 5208965.0
Test 8: # of page faults incurred by LRU: 9996
Test 8: # of page faults incurred by Blind Oracle: 9958
Test 8: # of page faults incurred by Combined Algorithm: 9996
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2773916.0
Test 9: # of page faults incurred by LRU: 41
Test 9: # of page faults incurred by Blind Oracle: 24
Test 9: # of page faults incurred by Combined Algorithm: 37
Test 9: executed successfully!
Test 9: Has completed

+------------------------------------------------------------------------------------+
| Sample 2                                                                           |
+------------------------------------------------------------------------------------+

Testing Suite for Low Threshold values:


Test 1: Executing test ...
Prediction error: 1.5188059E7
Test 1: # of page faults incurred by LRU: 6995
Test 1: # of page faults incurred by Blind Oracle: 6745
Test 1: # of page faults incurred by Combined Algorithm: 6748
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.254768016E9
Test 2: # of page faults incurred by LRU: 9887
Test 2: # of page faults incurred by Blind Oracle: 9976
Test 2: # of page faults incurred by Combined Algorithm: 9978
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.243021935E9
Test 3: # of page faults incurred by LRU: 305
Test 3: # of page faults incurred by Blind Oracle: 4775
Test 3: # of page faults incurred by Combined Algorithm: 310
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.49282022E8
Test 4: # of page faults incurred by LRU: 9925
Test 4: # of page faults incurred by Blind Oracle: 5576
Test 4: # of page faults incurred by Combined Algorithm: 5578
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.48980382E8
Test 5: # of page faults incurred by LRU: 293
Test 5: # of page faults incurred by Blind Oracle: 183
Test 5: # of page faults incurred by Combined Algorithm: 185
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.4752223E7
Test 6: # of page faults incurred by LRU: 9991
Test 6: # of page faults incurred by Blind Oracle: 9972
Test 6: # of page faults incurred by Combined Algorithm: 9991
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2387254E7
Test 7: # of page faults incurred by LRU: 36
Test 7: # of page faults incurred by Blind Oracle: 47
Test 7: # of page faults incurred by Combined Algorithm: 42
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 4923920.0
Test 8: # of page faults incurred by LRU: 9991
Test 8: # of page faults incurred by Blind Oracle: 9961
Test 8: # of page faults incurred by Combined Algorithm: 9991
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2594761.0
Test 9: # of page faults incurred by LRU: 40
Test 9: # of page faults incurred by Blind Oracle: 24
Test 9: # of page faults incurred by Combined Algorithm: 26
Test 9: executed successfully!
Test 9: Has completed


Testing Suite for High Threshold values:


Test 1: Executing test ...
Prediction error: 1.5162932E7
Test 1: # of page faults incurred by LRU: 6970
Test 1: # of page faults incurred by Blind Oracle: 6672
Test 1: # of page faults incurred by Combined Algorithm: 6970
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.248442085E9
Test 2: # of page faults incurred by LRU: 10153
Test 2: # of page faults incurred by Blind Oracle: 10042
Test 2: # of page faults incurred by Combined Algorithm: 10153
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.244935625E9
Test 3: # of page faults incurred by LRU: 294
Test 3: # of page faults incurred by Blind Oracle: 4869
Test 3: # of page faults incurred by Combined Algorithm: 294
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.53516692E8
Test 4: # of page faults incurred by LRU: 9949
Test 4: # of page faults incurred by Blind Oracle: 5642
Test 4: # of page faults incurred by Combined Algorithm: 5675
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.51110555E8
Test 5: # of page faults incurred by LRU: 263
Test 5: # of page faults incurred by Blind Oracle: 207
Test 5: # of page faults incurred by Combined Algorithm: 263
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.4814567E7
Test 6: # of page faults incurred by LRU: 9994
Test 6: # of page faults incurred by Blind Oracle: 9974
Test 6: # of page faults incurred by Combined Algorithm: 9994
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2627775E7
Test 7: # of page faults incurred by LRU: 30
Test 7: # of page faults incurred by Blind Oracle: 16
Test 7: # of page faults incurred by Combined Algorithm: 29
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 5291446.0
Test 8: # of page faults incurred by LRU: 9999
Test 8: # of page faults incurred by Blind Oracle: 9965
Test 8: # of page faults incurred by Combined Algorithm: 9999
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2372944.0
Test 9: # of page faults incurred by LRU: 34
Test 9: # of page faults incurred by Blind Oracle: 23
Test 9: # of page faults incurred by Combined Algorithm: 34
Test 9: executed successfully!
Test 9: Has completed