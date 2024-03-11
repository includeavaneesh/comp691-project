Author:
Student Name: Avaneesh Kanshi
Student ID: 40273760
Contact E-mail: avaneeshkanshi@gmail.com, avaneesh.kanshi@live.concordia.ca

CONTENTS:
1. About the Project
2. Functionalities
    -> generateRandomSequence
    -> generateH
    -> addNoise
    -> blindOracle 
3. Testing
    -> Category: N > k
    -> Category: N = k + 1
    -> Category: N >> k
4. How to Execute the program
    -> Requirements  
    -> How to execute the script
5. Sample Outputs
    -> Sample 1
    -> Sample 2


1. About the project

The current state of the project implements a Blind Oracle algorithm for paging using locality of reference approach. 
In case of the optimal algorithm, the algorithm performs using the furthest in the future approach meaning that the 
page that is requested further in the future is evicted from the cache. To elaborate, the algorithm basically has the 
knowledge of when the next time the current page will be requested again in the future again. This knowledge is stored
in a list (h). The Blind Oracle is quite similar to the OPT (Furthest in the future) algorithm. It has a predictive knowledge
of when the next time the current page will be requested again in the future. This is calculated using some machine learning
model trained on previous data. When a page is requested, the algorithm looks for it in the cache. If there is such page in the
cache, then a page hit has occured. Moreover, each page in cache is updated with the most current predicted h-values. If such
page is not found in the cache, a cache miss occurs. The algorithm either finds an empty space and allocates it to the requested
page, or the page with the highest predicted h-value is evicted and the new page is put in place. 

2. Functionalities

The Main.java contains the implementation of the Blind Oracle class containing the following functionalities:

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


3. Testing

The functionalities of the blind oracle algorithm are tested on several edge cases and normal cases to evaluate and determine
the correctness of the behavior of the algorithm. The test cases can be divided into 3 categories of N = k + 1, N >> k and N > k.
The categories and different tests are discussed in detail below:

Note: It must be noted that each test is performed on different request sequence to maintain average performance throughout, as
comparing them on basis of same sequence could be biased to few scenarios performing the best. However, more detailed analysis
is required to evaluate the performance of the Blind oracle algorithm.

* Category: N > k
+------------------------------------------------------------------------------------+
| Test 1: test1()                                                                    |
+------------------------------------------------------------------------------------+
    This test cases is to check the normal behavior of the algorithm under average
    circumstances. With k being 10, and N being 100. The request sequence length is set
    at 10000.

    The probability of requesting a page in future that already is in the local cache is
    medium and the deviation in the predicted h-value is average as well. The algorithm
    in this case is expected to perform worse than the OPT.


* Category: N = k + 1
+------------------------------------------------------------------------------------+
| Test 2: test2()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., k + 1 = N. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to incur the highest
    number of page faults among all the scenarios.

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


* Category: N >> k
+------------------------------------------------------------------------------------+
| Test 6: test6()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the WORST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is significantly large than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is high. The algorithm is expected
    to incur a page fault in almost every request, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to incur the highest
    number of page faults among all the scenarios. Moreover, it could incure more page
    faults than test2(), as there are more variability of the pages to be requested
    as N >> k. 

+------------------------------------------------------------------------------------+
| Test 7: test7()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is high. The algorithm might
    incur a page fault in most requests, as a new page (not present in the local
    cache) is requested most of the times. Moreover, with the prediction error (can be
    observed in the sample outputs) being high, the algorithm is basically does not have 
    any knowledge of future inputs.

    Therefore, given the above scenario, the algorithm is expected to lower number of page 
    faults than previous test i.e., test6(), however performing worse than OPT as it
    is unable to predict future pages accurately.

+------------------------------------------------------------------------------------+
| Test 8: test8()                                                                    |
+------------------------------------------------------------------------------------+
    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very low and the deviation in the predicted h-value is low. The algorithm is expected
    to incur page faults on most number of requests, as the new page (not present in the local
    cache) is requested rarely. However, the tau value is low, the prediction error (can be
    observed in the sample outputs) is low. In this scenario, the algorithm has a better idea
    of what the future inputs could be.

    Therefore, given the above scenario, the algorithm would perform similar to that of
    test6().

+------------------------------------------------------------------------------------+
| Test 9: test9()                                                                    |
+------------------------------------------------------------------------------------+
    
    This is the BEST CASE SCENARIO for the category

    This test case is to check the behavior of the algorithm when the global memory size
    is slightly bigger than the local cache size i.e., N >> k. The request sequence 
    length is set at 100000. 

    The probability of requesting a page in future that already is in the local cache is
    very high and the deviation in the predicted h-value is low. The algorithm is expected
    to incur least number page faults on the requests, as the new page requested has a high
    probability of already being available in the cache. Moreover, the tau value is low, the 
    prediction error (can be observed in the sample outputs) is low. In this scenario, the 
    algorithm has a better idea of what the future inputs could be performing similar to OPT.

    Therefore, given the above scenario, the algorithm is expected to least number of page 
    faults.


4. How to Execute the program

+------------------------------------------------------------------------------------+
| Requirements                                                                       |
+------------------------------------------------------------------------------------+
     * The program can work with JDK version 20.0.2 and higher.
     * Java Version 17.0 is required for executing the program through Maven.

+------------------------------------------------------------------------------------+
| How to execute the script                                                          |
+------------------------------------------------------------------------------------+

    * Simple JVM Command (For windows):
        Step 1: Go into the package's root directory "cd src\main\java"
        Step 2: Compile the java classes using "javac com\comp691\Main.java"
        Step 3: To run the script, run the main class using the following command "java com.comp691.Main"

    * Using Maven (Version required: Apache Maven 3.9.6 https://maven.apache.org/download.cgi) 
        Step 1: Build the application using from the project's root directory"mvn clean package"
        Step 2: Locate the .jar file, usually found in the /target folder. You can use the following command "cd target"
        Step 3: Run the .jar file using "java -jar project-1.0-SNAPSHOT.jar"


5. Sample Outputs

+------------------------------------------------------------------------------------+
| Sample 1                                                                           |
+------------------------------------------------------------------------------------+

Test 1: Executing test ...
Prediction error: 1.7963844E7
Test 1: # of page faults incurred 7377
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.247434238E9
Test 2: # of page faults incurred 9818
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.247385437E9
Test 3: # of page faults incurred 4671
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.53158574E8
Test 4: # of page faults incurred 5670
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.49895849E8
Test 5: # of page faults incurred 235
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.523456E7
Test 6: # of page faults incurred 9972
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2511991E7
Test 7: # of page faults incurred 55
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 4982403.0
Test 8: # of page faults incurred 9966
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2544398.0
Test 9: # of page faults incurred 26
Test 9: executed successfully!
Test 9: Has completed

+------------------------------------------------------------------------------------+
| Sample 2                                                                           |
+------------------------------------------------------------------------------------+

Test 1: Executing test ...
Prediction error: 1.7984814E7
Test 1: # of page faults incurred 7415
Test 1: executed successfully!
Test 1: Has completed

Test 2: Executing test ...
Prediction error: 2.242774739E9
Test 2: # of page faults incurred 9978
Test 2: executed successfully!
Test 2: Has completed

Test 3: Executing test ...
Prediction error: 2.249497257E9
Test 3: # of page faults incurred 5170
Test 3: executed successfully!
Test 3: Has completed

Test 4: Executing test ...
Prediction error: 2.52696853E8
Test 4: # of page faults incurred 5622
Test 4: executed successfully!
Test 4: Has completed

Test 5: Executing test ...
Prediction error: 2.49748155E8
Test 5: # of page faults incurred 218
Test 5: executed successfully!
Test 5: Has completed

Test 6: Executing test ...
Prediction error: 4.4852662E7
Test 6: # of page faults incurred 9967
Test 6: executed successfully!
Test 6: Has completed

Test 7: Executing test ...
Prediction error: 2.2406895E7
Test 7: # of page faults incurred 144
Test 7: executed successfully!
Test 7: Has completed

Test 8: Executing test ...
Prediction error: 5257160.0
Test 8: # of page faults incurred 9951
Test 8: executed successfully!
Test 8: Has completed

Test 9: Executing test ...
Prediction error: 2441381.0
Test 9: # of page faults incurred 24
Test 9: executed successfully!
Test 9: Has completed