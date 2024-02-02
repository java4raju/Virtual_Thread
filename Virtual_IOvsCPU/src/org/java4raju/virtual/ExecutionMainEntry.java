package org.java4raju.virtual;

import org.java4raju.virtual.process.CompletableFutureExecution;
import org.java4raju.virtual.process.SequentialExecution;
import org.java4raju.virtual.process.ThreadExecution;
import org.java4raju.virtual.process.VirtualThreadExecution;


public class ExecutionMainEntry {
    public static void main(String[] args) {

        int USER_COUNT = 1000;
        long sleepTime = 2;
        
        boolean isIoIntensiveTask = false;
      
        if(isIoIntensiveTask)
        System.out.print("Executing IO Intensive Task ");
        else
        System.out.print("Executing CPU Bound Task ");
        
        
        System.out.println("for "+USER_COUNT+" users!\n");
        
        System.out.println("Sequential execution total time :      " 
        + new SequentialExecution().executeSequentially(USER_COUNT, sleepTime, isIoIntensiveTask));
        
        System.out.println("Thread Action total time :             " 
        + new ThreadExecution().executeUsingPlatformThread(USER_COUNT, sleepTime, isIoIntensiveTask));
        
        System.out.println("Completable Future Action total time : " 
        + new CompletableFutureExecution().executeUsingCompletableFuture(USER_COUNT, sleepTime, isIoIntensiveTask));
        
        System.out.println("Virtual Thread Action time :           " 
        + new VirtualThreadExecution().executeUsingVirtualThread(USER_COUNT, sleepTime, isIoIntensiveTask));
    }
}