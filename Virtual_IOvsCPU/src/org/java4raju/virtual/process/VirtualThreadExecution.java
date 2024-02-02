package org.java4raju.virtual.process;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.java4raju.virtual.task.CPUIntensiveTask;
import org.java4raju.virtual.task.IOIntensiveTask;

public class VirtualThreadExecution {
	
	Set<String> pThreads = ConcurrentHashMap.newKeySet();

	public Long executeUsingVirtualThread(int iterations, long sleepTime, boolean isIOIntensive) {
		 
		ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
		Future<?>[] virtualThreads = new Future[iterations];

		Instant actionStart = Instant.now();

		// Create and start multiple threads in a loop
		for (int i = 0; i < iterations; i++) {
			final int index = i;
			virtualThreads[i] = executor.submit(() -> {
		    
			 pThreads.add(Thread.currentThread().toString().substring(25));
			 
			 try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(isIOIntensive) {
				IOIntensiveTask ioIntensiveTask = new IOIntensiveTask();
				ioIntensiveTask.runIOIntensiveTask("C:\\Users\\t0265602\\Documents\\execution\\poc\\reactiveJava\\Virtual_IOvsCPU\\resources\\File.txt");
				}
				else {
				CPUIntensiveTask cpuInt = new CPUIntensiveTask();
				cpuInt.runCPUIntensiveTask();
				}
			});
		}
		
		for (Future thread : virtualThreads) {
			try {
				thread.get(); // Wait for thread to complete
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\nTotal Plateform Thread created: "+pThreads.size());
		executor.shutdown();
		return Duration.between(actionStart, Instant.now()).toMillis();
	}
}
