package org.java4raju.virtual.task;

/**
 * CPU Intensive task
 */
public class CPUIntensiveTask {

    public void runCPUIntensiveTask(){
        int startNumber = 1;
        int endNumber = 1000000; // should adjust this range based on your machine's capabilities
        calculateSumOfSquares(startNumber, endNumber);
    }

    private static long calculateSumOfSquares(int start, int end) {
        long sum = 0;

        for (int i = start; i <= end; i++) {
            sum += (long) i * i;
        }

        return sum;
    }

}
