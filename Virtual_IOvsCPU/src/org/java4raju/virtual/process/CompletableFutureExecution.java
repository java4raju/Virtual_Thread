package org.java4raju.virtual.process;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.java4raju.virtual.task.CPUIntensiveTask;
import org.java4raju.virtual.task.IOIntensiveTask;

public class CompletableFutureExecution {

	public Long executeUsingCompletableFuture(int iterations, long sleepTime, boolean isIOIntensive) {

		// Create an array of CompletableFuture
		CompletableFuture<String>[] completableFutures = new CompletableFuture[iterations];

		Instant actionStart = Instant.now();

		// Execute tasks asynchronously in a loop
		for (int i = 0; i < iterations; i++) {
			final int index = i;
			completableFutures[i] = CompletableFuture.supplyAsync(() -> {

				try {
					Thread.sleep(sleepTime);

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				if(isIOIntensive) {
				IOIntensiveTask ioIntensiveTask = new IOIntensiveTask();
				ioIntensiveTask.runIOIntensiveTask("C:\\Users\\t0265602\\Documents\\execution\\poc\\reactiveJava\\Virtual_IOvsCPU\\resources\\File.txt");
				}
				else{
				CPUIntensiveTask cpuInt = new CPUIntensiveTask();
				cpuInt.runCPUIntensiveTask();
				}
				return "Task " + index;
			});
		}

		// Wait for all CompletableFuture to complete and collect the results
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(completableFutures);

		// Get the results from all CompletableFuture
		try {
			allFutures.get(); // Wait for all tasks to complete
			for (CompletableFuture<String> future : completableFutures) {
				future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return Duration.between(actionStart, Instant.now()).toMillis();
	}

}
